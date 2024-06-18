package com.web.movie.controller;

import com.web.movie.exception.BadRequestException;
import com.web.movie.exception.ResourceNotFoundException;
import com.web.movie.payload.request.UpdatePasswordRequest;
import com.web.movie.payload.request.UpdateProfileUserRequest;
import com.web.movie.payload.response.UserResponse;
import com.web.movie.service.iterface.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @PostMapping("/users/update-image")
    public ResponseEntity<?> uploadImage(@RequestParam("file")MultipartFile file){
        log.info("{}", file.getContentType());
        String imgUrl = userService.uploadImage(file);
        return new ResponseEntity<>(imgUrl, HttpStatus.CREATED);
    }

    @PutMapping("/users/update-profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileUserRequest request) {
        UserResponse response = userService.updateProfile(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/users/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        try {
            log.info("{} - {} - {}", request.getOldPassword(), request.getNewPassword(), request.getConfirmPassword());
            userService.changePassword(request);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (BadRequestException e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }catch (ResourceNotFoundException e){
            return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
