package com.web.movie.service.implement;

import com.web.movie.entity.Episode;
import com.web.movie.mapper.EpisodeMapper;
import com.web.movie.payload.dto.EpisodeDTO;
import com.web.movie.repository.EpisodeRepository;
import com.web.movie.service.iterface.IEpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EpisodeService implements IEpisodeService {
    private final EpisodeRepository episodeRepository;
    private final EpisodeMapper episodeMapper;
    @Override
    public List<EpisodeDTO> getEpisodesByFilmId(Integer id) {
        return episodeRepository.findByFilm_IdAndStatusOrderByEpisodeOrder(id, true);
    }

    @Override
    public EpisodeDTO getEpisodeByOrderEpisode(Integer id, boolean status, String tap) {
        if(tap == null){
            return null;
        }
        if(tap.equals("full")){
            return episodeMapper.toEpisodeDTO(episodeRepository.findByFilm_IdAndStatusAndEpisodeOrder(id, status, 1).orElse(null));
        } else {
            return episodeMapper.toEpisodeDTO(episodeRepository.findByFilm_IdAndStatusAndEpisodeOrder(id, status, Integer.parseInt(tap)).orElse(null));
        }
    }
}
