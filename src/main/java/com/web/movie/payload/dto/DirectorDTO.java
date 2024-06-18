package com.web.movie.payload.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DirectorDTO {
    Integer id;
    String name;
    String description;
    String avatar;
    Date birthday;
}
