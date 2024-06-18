package com.web.movie.payload.dto;

import com.web.movie.entity.enumType.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    Integer id;
    String name;
    String email;
    String phone;
    String avatar;
    UserRole role;
    Boolean enabled;
}
