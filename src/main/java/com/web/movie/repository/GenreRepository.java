package com.web.movie.repository;

import com.web.movie.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    Optional<Genre> findBySlug(String slug);
    Set<Genre> findByIdIn(Set<Integer> genreIds);
}
