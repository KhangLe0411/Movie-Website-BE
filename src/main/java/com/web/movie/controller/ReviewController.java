package com.web.movie.controller;

import com.web.movie.mapper.ReviewMapper;
import com.web.movie.payload.dto.ReviewDTO;
import com.web.movie.payload.request.ReviewRequest;
import com.web.movie.service.iterface.IReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {
    private final IReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @PostMapping("/review/create")
    public ResponseEntity<?> createReview(@Valid @RequestBody ReviewRequest request){
        try{
            ReviewDTO reviewDTO = reviewMapper.toReviewDTO(reviewService.createReview(request));
            return new ResponseEntity<>(reviewDTO, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/review/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Integer id,
                                          @Valid @RequestBody ReviewRequest request){
        try{
            ReviewDTO reviewDTO = reviewMapper.toReviewDTO(reviewService.updateReview(request, id));
            return new ResponseEntity<>(reviewDTO, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/review/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer id){
        try{
            reviewService.deleteReview(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
