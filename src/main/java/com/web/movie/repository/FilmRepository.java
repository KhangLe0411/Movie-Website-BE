package com.web.movie.repository;

import com.web.movie.entity.Film;
import com.web.movie.entity.enumType.FilmAccessType;
import com.web.movie.entity.enumType.FilmType;
import com.web.movie.payload.dto.FilmDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FilmRepository extends JpaRepository<Film, Integer> {
    // Tìm kiếm phim theo type, accessType, status -> sắp xếp theo publishedAt giảm dần -> phân trang -> return Page<FilmDto>
    @Query("SELECT new com.web.movie.payload.dto.FilmDTO(f.id, f.title, f.slug, f.poster, f.type, f.accessType, f.rating, f.price, f.status, f.trailerUrl) FROM Film f WHERE f.type = ?1 AND f.accessType = ?2 AND f.status = ?3")
    Page<FilmDTO> findByTypeAndAccessTypeAndStatus(FilmType filmType, FilmAccessType filmAccessType, Boolean status, Pageable pageable);

    @Query("SELECT new com.web.movie.payload.dto.FilmDTO(f.id, f.title, f.slug, f.poster, f.type, f.accessType, f.rating, f.price, f.status, f.trailerUrl) FROM Film f WHERE f.accessType = ?1 AND f.status = ?2 ORDER BY f.publishedAt DESC")
    Page<FilmDTO> findByAccessTypeAndStatus(FilmAccessType filmAccessType, Boolean status, Pageable pageable);

    @Query("SELECT new com.web.movie.payload.dto.FilmDTO(f.id, f.title, f.slug, f.poster, f.type, f.accessType, f.rating, f.price, f.status, f.trailerUrl) FROM Film f JOIN f.genres g WHERE f.accessType = ?1 AND f.status = ?2 AND g.slug = ?3 ORDER BY f.publishedAt DESC")
    Page<FilmDTO> findByAccessTypeAndStatusAndGenres_SlugOrderByPublishedAtDesc(FilmAccessType filmAccessType, Boolean status, String genreSlug, Pageable pageable);

    @Query("SELECT new com.web.movie.payload.dto.FilmDTO(f.id, f.title, f.slug, f.poster, f.type, f.accessType, f.rating, f.price, f.status, f.trailerUrl) FROM Film f JOIN f.country c WHERE f.accessType = ?1 AND f.status = ?2 AND c.slug = ?3 ORDER BY f.publishedAt DESC")
    Page<FilmDTO> findByAccessTypeAndStatusAndCountry_SlugOrderByPublishedAtDesc(FilmAccessType filmAccessType, Boolean status, String countrySlug, Pageable pageable);

    Optional<Film> findByIdAndSlugAndAccessType(Integer id, String slug, FilmAccessType filmAccessType);
}
