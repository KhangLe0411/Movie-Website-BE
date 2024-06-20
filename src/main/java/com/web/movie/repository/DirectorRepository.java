package com.web.movie.repository;

import com.web.movie.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface DirectorRepository extends JpaRepository<Director, Integer> {
    Set<Director> findByIdIn(Set<Integer> directorIds);
}
