package com.web.movie.service.implement;

import com.web.movie.entity.User;
import com.web.movie.exception.BadRequestException;
import com.web.movie.exception.ResourceNotFoundException;
import com.web.movie.payload.request.UpdatePasswordRequest;
import com.web.movie.payload.request.UpdateProfileUserRequest;
import com.web.movie.payload.response.UserResponse;
import com.web.movie.repository.UserRepository;
import com.web.movie.service.iterface.IFileService;
import com.web.movie.service.iterface.IUserService;
import com.web.movie.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final IFileService fileService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserResponse updateProfile(UpdateProfileUserRequest request) {
        User user = SecurityUtils.getCurrentUserLogin();
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        userRepository.save(user);

        return new UserResponse(user.getAvatar(), user.getName(), user.getPhone());
    }

    @Override
    public String uploadImage(MultipartFile file) {
        var currentUser = SecurityUtils.getCurrentUserLogin();
        String imgUrl = fileService.uploadFile(file);

        if(currentUser.getAvatar() != null){
            // Xóa file cũ
            fileService.deleteFile(currentUser.getAvatar());
        }

        currentUser.setAvatar(imgUrl);
        userRepository.save(currentUser);

        return imgUrl;
    }

    @Override
    public void changePassword(UpdatePasswordRequest request) {
        // Get current user log in
        User user = SecurityUtils.getCurrentUserLogin();

        if(user == null){
            throw new ResourceNotFoundException("Không tìm thấy user");
        }

        // check old password correct
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Mật khẩu cũ không đúng");
        }

        // check new password and confirm password match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Mật khẩu mới và xác nhận mật khẩu không khớp");
        }

        // check new password and old password match
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("Mật khẩu mới không được trùng với mật khẩu cũ");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
