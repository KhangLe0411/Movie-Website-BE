package com.web.movie.service.iterface;

import com.web.movie.entity.Film;
import com.web.movie.entity.enumType.FilmAccessType;
import com.web.movie.entity.enumType.FilmType;
import com.web.movie.payload.dto.FilmDTO;
import com.web.movie.payload.dto.FilmDetailDTO;
import com.web.movie.payload.dto.ReviewDTO;
import com.web.movie.payload.request.CreateFilmRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IFilmService {
    Page<FilmDTO> getFilmsByType(FilmType filmType, FilmAccessType filmAccessType, Boolean status, Integer page, Integer limit);

    Page<FilmDTO> getFilmsByAccessType(FilmAccessType filmAccessType, Boolean status, Integer page, Integer limit);

    Page<FilmDTO> getFilmsOfGenre(String slug, FilmAccessType filmAccessType, Boolean status, Integer page, Integer limit);

    Page<FilmDTO> getFilmsOfCountry(String slug, FilmAccessType filmAccessType, Boolean status, Integer page, Integer limit);

    FilmDetailDTO findFilmByIdAndSlug(Integer id, String slug, FilmAccessType accessType);

    List<Film> getAllFilms();
    Film createFilm(CreateFilmRequest request);
    Film getFilmById(Integer filmId);
}
