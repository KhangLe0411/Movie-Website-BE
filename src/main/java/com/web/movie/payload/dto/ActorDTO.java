package com.web.movie.payload.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActorDTO {
    Integer id;
    String name;
    String description;
    String avatar;
    Date birthday;
}
