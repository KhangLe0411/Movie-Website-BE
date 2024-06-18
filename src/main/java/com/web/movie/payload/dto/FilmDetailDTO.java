package com.web.movie.payload.dto;

import com.web.movie.entity.enumType.FilmAccessType;
import com.web.movie.entity.enumType.FilmType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmDetailDTO {
    Integer id;
    String title;
    String slug;
    String description;
    String poster;
    Integer releaseYear;
    Integer view;
    Double rating;
    FilmType type;
    FilmAccessType accessType;
    Integer price;
    Boolean status;
    String trailerUrl;
    Date publishedAt;
    CountryDTO country;
    Set<GenreDTO> genres;
    Set<ActorDTO> actors;
    Set<DirectorDTO> directors;
}
