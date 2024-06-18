package com.web.movie.mapper;

import com.web.movie.entity.Film;
import com.web.movie.payload.dto.FilmDTO;
import com.web.movie.payload.dto.FilmDetailDTO;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.ComponentScan;

@Mapper(componentModel = "spring")
@ComponentScan({"com.web.movie.mapper"})
public interface FilmMapper {
    FilmDetailDTO toFilmDetailDTO(Film film);
    FilmDTO toFilmDTO(Film film);
}
