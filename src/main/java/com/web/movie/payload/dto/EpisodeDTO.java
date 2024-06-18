package com.web.movie.payload.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EpisodeDTO {
    Integer id;
    Integer orderEpisode; // Tập 1, Tập 2, Tập 3, ...
    String title; // Tập 1: Tên tập phim
    Boolean status; // true: Đã phát sóng, false: Chưa phát sóng
    Integer duration;
    String videoUrl;
}
