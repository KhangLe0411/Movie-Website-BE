package com.web.movie.repository;

import com.web.movie.entity.Episode;
import com.web.movie.payload.dto.EpisodeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<Episode, Integer> {

    @Query("SELECT new com.web.movie.payload.dto.EpisodeDTO(e.id, e.orderEpisode, e.title, e.status, e.video.duration, e.video.url) FROM Episode e WHERE e.film.id = ?1 AND e.status = ?2 ORDER BY e.orderEpisode ASC")
    List<EpisodeDTO> findByFilm_IdAndStatusOrderByEpisodeOrder(Integer id, boolean status);

    @Query("SELECT e FROM Episode e WHERE e.film.id = ?1 AND e.status = ?2 AND e.orderEpisode = ?3")
    Optional<Episode> findByFilm_IdAndStatusAndEpisodeOrder(Integer id, boolean status, Integer episodeOrder);
}
