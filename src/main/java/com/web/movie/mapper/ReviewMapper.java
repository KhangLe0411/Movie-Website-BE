package com.web.movie.mapper;

import com.web.movie.entity.Review;
import com.web.movie.payload.dto.ReviewDTO;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.ComponentScan;

@Mapper(componentModel = "spring")
@ComponentScan({"com.web.movie.mapper"})
public interface ReviewMapper {
    ReviewDTO toReviewDTO(Review review);
}
