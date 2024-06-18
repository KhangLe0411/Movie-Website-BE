package com.web.movie.payload.response;

import com.web.movie.payload.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class BearerToken {
//    private String name;
//    private String email;
//    private String image;
//    private String phone;
    private UserDTO user;
    private String accessToken;
    private String tokenType;
    private List<String> roles;
}
