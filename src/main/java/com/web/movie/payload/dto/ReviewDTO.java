package com.web.movie.payload.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewDTO {
    Integer id;
    Integer rating;
    String comment;
    Date createdAt;
    Date updatedAt;
    UserDTO user;
}
