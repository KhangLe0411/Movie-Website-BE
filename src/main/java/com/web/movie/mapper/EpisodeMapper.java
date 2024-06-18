package com.web.movie.mapper;

import com.web.movie.entity.Episode;
import com.web.movie.payload.dto.EpisodeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.ComponentScan;

@Mapper(componentModel = "spring")
@ComponentScan({"com.web.movie.mapper"})
public interface EpisodeMapper{
    @Mapping(target = "videoUrl", source = "video.url")
    @Mapping(target = "duration", source = "video.duration")
    EpisodeDTO toEpisodeDTO(Episode episode);
}
