package com.web.movie.mapper;

import com.web.movie.entity.User;
import com.web.movie.payload.dto.UserDTO;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.ComponentScan;

@Mapper(componentModel = "spring")
@ComponentScan({"com.web.movie.mapper"})
public interface UserMapper {
    UserDTO toUserDTO(User user);
}
