package com.web.movie.service.implement;

import com.web.movie.entity.Film;
import com.web.movie.entity.Review;
import com.web.movie.entity.User;
import com.web.movie.exception.BadRequestException;
import com.web.movie.exception.ResourceNotFoundException;
import com.web.movie.payload.dto.ReviewDTO;
import com.web.movie.payload.request.ReviewRequest;
import com.web.movie.repository.FilmRepository;
import com.web.movie.repository.ReviewRepository;
import com.web.movie.service.iterface.IReviewService;
import com.web.movie.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final FilmRepository filmRepository;

    @Override
    public Review createReview(ReviewRequest request) {
        User user = SecurityUtils.getCurrentUserLogin();

        Film film = filmRepository.findById(request.getFilmId())
                .orElseThrow(()->new ResourceNotFoundException("Không tìm thấy phim có id" + request.getFilmId()));

        Review review = Review.builder()
                .user(user)
                .comment(request.getComment())
                .rating(request.getRating())
                .film(film)
                .build();
        return reviewRepository.save(review);
    }

    @Override
    public Review updateReview(ReviewRequest request, Integer id) {
        User user = SecurityUtils.getCurrentUserLogin();

        Film film = filmRepository.findById(request.getFilmId())
                .orElseThrow(()->new ResourceNotFoundException("Không tìm thấy phim có id" + request.getFilmId()));

        Review review = reviewRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Không tìm thấy review có id" + id));

        // check user is owner of review
        if (!review.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Bạn không có quyền sửa review này");
        }

        review.setFilm(film);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Integer id) {
        User user = SecurityUtils.getCurrentUserLogin();

        Review review = reviewRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Không tìm thấy review có id: " + id));

        if (!review.getUser().getId().equals(user.getId())){
            throw new BadRequestException("Bạn không có quyền xóa review này");
        }

        reviewRepository.delete(review);
    }

    @Override
    public List<ReviewDTO> getReviewsOfFilm(Integer id) {
        return reviewRepository.findByFilm_IdOrderByCreatedAtDesc(id);
    }
}
