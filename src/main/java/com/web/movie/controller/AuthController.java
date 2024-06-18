package com.web.movie.controller;

import com.web.movie.exception.ResourceNotFoundException;
import com.web.movie.payload.request.LoginRequest;
import com.web.movie.payload.request.RegisterRequest;
import com.web.movie.payload.response.BearerToken;
import com.web.movie.service.implement.LogoutService;
import com.web.movie.service.iterface.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;
    private final LogoutService logoutService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) throws BadRequestException {
        log.info("Login request: {}", request);
        try{
            BearerToken response = authService.login(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DisabledException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register request: {}", request);
        authService.register(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) {
        try{
            log.info(email);
            authService.forgotPassword(email);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e){
            return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
        } catch (Exception e){
            log.error("Error sending forgot password email", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        log.info(request.getHeader("Authorization"));
        logoutService.logout(request, response, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
