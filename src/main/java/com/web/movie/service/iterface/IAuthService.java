package com.web.movie.service.iterface;

import com.web.movie.payload.request.LoginRequest;
import com.web.movie.payload.request.RegisterRequest;
import com.web.movie.payload.request.UpdatePasswordRequest;
import com.web.movie.payload.response.BearerToken;
import org.apache.coyote.BadRequestException;

public interface IAuthService {
    BearerToken login(LoginRequest request) throws BadRequestException;

    void register(RegisterRequest request);

    void forgotPassword(String email);
}
