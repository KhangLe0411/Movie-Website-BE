package com.web.movie.service.iterface;

import com.web.movie.entity.Episode;
import com.web.movie.payload.dto.EpisodeDTO;

import java.util.List;

public interface IEpisodeService {
    List<EpisodeDTO> getEpisodesByFilmId(Integer id);

    EpisodeDTO getEpisodeByOrderEpisode(Integer id, boolean status, String tap);
}
