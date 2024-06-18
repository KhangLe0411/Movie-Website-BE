package com.web.movie.service.implement;

import com.web.movie.entity.Film;
import com.web.movie.entity.enumType.FilmAccessType;
import com.web.movie.entity.enumType.FilmType;
import com.web.movie.mapper.FilmMapper;
import com.web.movie.payload.dto.FilmDTO;
import com.web.movie.payload.dto.FilmDetailDTO;
import com.web.movie.repository.FilmRepository;
import com.web.movie.service.iterface.IFilmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService implements IFilmService {
    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;
    @Override
    public Page<FilmDTO> getFilmsByType(FilmType filmType, FilmAccessType filmAccessType, Boolean status, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("publishedAt").descending());
        return filmRepository.findByTypeAndAccessTypeAndStatus(filmType, filmAccessType, true, pageable);
    }

    @Override
    public Page<FilmDTO> getFilmsByAccessType(FilmAccessType filmAccessType, Boolean status, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("publishedAt").descending());
        return filmRepository.findByAccessTypeAndStatus(filmAccessType, status, pageable);
    }

    @Override
    public Page<FilmDTO> getFilmsOfGenre(String slug, FilmAccessType filmAccessType, Boolean status, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return filmRepository.findByAccessTypeAndStatusAndGenres_SlugOrderByPublishedAtDesc(filmAccessType, status, slug, pageable);
    }

    @Override
    public Page<FilmDTO> getFilmsOfCountry(String slug, FilmAccessType filmAccessType, Boolean status, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return filmRepository.findByAccessTypeAndStatusAndCountry_SlugOrderByPublishedAtDesc(filmAccessType, status, slug, pageable);
    }

    @Override
    public FilmDetailDTO findFilmByIdAndSlug(Integer id, String slug, FilmAccessType accessType) {
        return filmMapper.toFilmDetailDTO(filmRepository.findByIdAndSlugAndAccessType(id, slug, accessType).orElse(null));
    }
}
