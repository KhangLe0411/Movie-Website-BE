package com.web.movie.service.iterface;

import com.web.movie.entity.Review;
import com.web.movie.payload.dto.ReviewDTO;
import com.web.movie.payload.request.ReviewRequest;

import java.util.List;

public interface IReviewService {
    Review createReview(ReviewRequest request);
    Review updateReview(ReviewRequest request, Integer id);
    void deleteReview(Integer id);
    List<ReviewDTO> getReviewsOfFilm(Integer id);
}
