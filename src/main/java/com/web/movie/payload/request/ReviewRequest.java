package com.web.movie.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewRequest {
    @NotNull(message = "Rating không được để trống")
    Integer rating;
    @NotNull(message = "Comment không được để trống")
    String comment;
    @NotNull(message = "filmId không được để trống")
    Integer filmId;
}
