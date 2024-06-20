package com.web.movie.repository;

import com.web.movie.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ActorRepository extends JpaRepository<Actor, Integer> {
    Set<Actor> findByIdIn(Set<Integer> actorIds);
}
