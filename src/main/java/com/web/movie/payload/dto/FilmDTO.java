package com.web.movie.payload.dto;

import com.web.movie.entity.enumType.FilmAccessType;
import com.web.movie.entity.enumType.FilmType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmDTO {
    Integer id;
    String title;
    String slug;
    String poster;
    FilmType type;
    FilmAccessType accessType;
    Double rating;
    Integer price;
    Boolean status;
    String trailerUrl;
}
