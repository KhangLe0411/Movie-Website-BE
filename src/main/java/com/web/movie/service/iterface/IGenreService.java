package com.web.movie.service.iterface;

import com.web.movie.entity.Genre;

import java.util.List;

public interface IGenreService {
    Genre getGenreBySlug(String slug);
    List<Genre> findAllGenre();
}
