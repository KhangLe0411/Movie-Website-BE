package com.web.movie.payload.request;

import com.web.movie.entity.enumType.FilmAccessType;
import com.web.movie.entity.enumType.FilmType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateFilmRequest {
    @NotEmpty(message = "Tên phim không được để trống")
    String title;

    String description;

    @NotNull(message = "Năm phát hành không được để trống")
    Integer releaseYear;

    @NotNull(message = "Poster không được để trống")
    String poster;

    @NotNull(message = "Thể loại không được để trống")
    FilmType type;

    @NotNull(message = "Trạng thái không được để trống")
    Boolean status;

    @NotNull(message = "Quốc gia không được để trống")
    Integer countryId;

    Set<Integer> genreIds;
    Set<Integer> directorIds;
    Set<Integer> actorIds;

    @NotNull(message = "Loại truy cập không được để trống")
    FilmAccessType accessType;

    Integer price;
}
