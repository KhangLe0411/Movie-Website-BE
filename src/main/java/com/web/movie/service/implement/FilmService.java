package com.web.movie.service.implement;

import com.github.slugify.Slugify;
import com.web.movie.entity.*;
import com.web.movie.entity.enumType.FilmAccessType;
import com.web.movie.entity.enumType.FilmType;
import com.web.movie.exception.ResourceNotFoundException;
import com.web.movie.mapper.FilmMapper;
import com.web.movie.payload.dto.FilmDTO;
import com.web.movie.payload.dto.FilmDetailDTO;
import com.web.movie.payload.request.CreateFilmRequest;
import com.web.movie.repository.*;
import com.web.movie.service.iterface.IFilmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService implements IFilmService {
    private final FilmRepository filmRepository;
    private final CountryRepository countryRepository;
    private final GenreRepository genreRepository;
    private final DirectorRepository directorRepository;
    private final ActorRepository actorRepository;
    private final FilmMapper filmMapper;
    private final Slugify slugify;
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

    @Override
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    @Override
    public Film createFilm(CreateFilmRequest request) {
        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(()->new ResourceNotFoundException("Không tìm thấy quốc gia có id = " + request.getCountryId()));

        Set<Genre> genres = genreRepository.findByIdIn(request.getGenreIds());
        Set<Director> directors = directorRepository.findByIdIn(request.getDirectorIds());
        Set<Actor> actors = actorRepository.findByIdIn(request.getActorIds());

        Film film = Film.builder()
                .title(request.getTitle())
                .slug(slugify.slugify(request.getTitle()))
                .description(request.getDescription())
                .releaseYear(request.getReleaseYear())
                .poster(request.getPoster())
                .type(request.getType())
                .status(request.getStatus())
                .country(country)
                .accessType(request.getAccessType())
                .price(request.getPrice())
                .genres(genres)
                .directors(directors)
                .actors(actors)
                .build();

        return filmRepository.save(film);
    }

    @Override
    public Film getFilmById(Integer filmId) {
        return filmRepository.findById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phim có id = " + filmId));
    }
}
