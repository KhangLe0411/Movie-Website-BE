package com.web.movie.repository;

import com.web.movie.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Integer> {
    Optional<Country> findBySlug(String slug);
}
