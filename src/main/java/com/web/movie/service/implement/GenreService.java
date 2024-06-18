package com.web.movie.service.implement;

import com.web.movie.entity.Genre;
import com.web.movie.exception.ResourceNotFoundException;
import com.web.movie.repository.GenreRepository;
import com.web.movie.service.iterface.IGenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService implements IGenreService {
    private final GenreRepository genreRepository;
    @Override
    public Genre getGenreBySlug(String slug) {
        return genreRepository.findBySlug(slug)
                .orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy thể loại có slug = " + slug));
    }

    @Override
    public List<Genre> findAllGenre() {
        return genreRepository.findAll();
    }
}
