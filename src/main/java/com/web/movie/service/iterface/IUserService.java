package com.web.movie.service.iterface;

import com.web.movie.payload.request.UpdatePasswordRequest;
import com.web.movie.payload.request.UpdateProfileUserRequest;
import com.web.movie.payload.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {
    UserResponse updateProfile(UpdateProfileUserRequest request);

    String uploadImage(MultipartFile file);
    void changePassword(UpdatePasswordRequest request);
}
