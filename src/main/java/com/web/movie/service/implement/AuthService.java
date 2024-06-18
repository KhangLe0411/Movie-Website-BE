package com.web.movie.service.implement;

import com.web.movie.entity.TokenConfirm;
import com.web.movie.entity.User;
import com.web.movie.entity.UserAuthentication;
import com.web.movie.entity.enumType.TokenType;
import com.web.movie.entity.enumType.UserRole;
import com.web.movie.exception.BadRequestException;
import com.web.movie.exception.ResourceNotFoundException;
import com.web.movie.mapper.UserMapper;
import com.web.movie.payload.request.LoginRequest;
import com.web.movie.payload.request.RegisterRequest;
import com.web.movie.payload.response.BearerToken;
import com.web.movie.repository.TokenConfirmRepository;
import com.web.movie.repository.UserAuthenticationRepository;
import com.web.movie.repository.UserRepository;
import com.web.movie.security.JwtUtilities;
import com.web.movie.service.iterface.IAuthService;
import com.web.movie.service.iterface.IEmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final UserRepository userRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilities jwtUtilities;
    private final AuthenticationManager authenticationManager;
    private final TokenConfirmRepository tokenConfirmRepository;
    private final IEmailService mailService;
    private final UserMapper userMapper;
    @Override
    public BearerToken login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmailIgnoreCase(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        List<String> roleNames = new ArrayList<>();
        roleNames.add(user.getRole().toString());

        UserAuthentication userAuthentication = userAuthenticationRepository.findByUser(user);
        // If user_authentication table not exist or token expired
        if (userAuthentication == null || jwtUtilities.isTokenExpired(userAuthentication.getExpiredAt())){
            // Generate new token
            String token = jwtUtilities.generateToken(user.getEmail(), roleNames);

            // Create user_authentication table if not exist
            if (userAuthentication == null) {
                userAuthentication = new UserAuthentication();
                userAuthentication.setUser(user);
                userAuthentication.setModifiedAt(null);
            }

            // Set token & expiredAt
            userAuthentication.setToken(token);
            userAuthentication.setExpiredAt(jwtUtilities.extractExpiration(token));

            userAuthenticationRepository.save(userAuthentication);
            return new BearerToken(userMapper.toUserDTO(user), token, "Bearer", roleNames);
        }
        // Default user_authentication exist and token valid
        return new BearerToken(userMapper.toUserDTO(user), userAuthentication.getToken(), "Bearer", roleNames);
    }

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new BadRequestException("Email đã tồn tại");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())){
            throw new BadRequestException("Mật khẩu không khớp");
        } else {
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setName(request.getName());
            user.setRole(UserRole.USER);
            user.setEnabled(false);
            userRepository.save(user);
            log.info("New user registered: {}", user);

            TokenConfirm tokenConfirm = new TokenConfirm();
            tokenConfirm.setToken(UUID.randomUUID().toString());
            tokenConfirm.setUser(user);
            tokenConfirm.setType(TokenType.EMAIL_VERIFICATION);
            tokenConfirm.setExpiryDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
            tokenConfirmRepository.save(tokenConfirm);
            log.info("Token confirm created: {}", tokenConfirm);

            Map<String, String> data = new HashMap<>();
            data.put("email", user.getEmail());
            data.put("username", user.getName());
            data.put("token", tokenConfirm.getToken());
            mailService.sendMailConfirmRegistration(data);
            log.info("Email sent to: {}", user.getEmail());
        }
    }

    @Transactional
    public Map<String, Object> confirmEmail(String token){
        Map<String, Object> data = new HashMap<>();
        Optional<TokenConfirm> tokenConfirmOptional = tokenConfirmRepository
                .findByTokenAndType(token, TokenType.EMAIL_VERIFICATION);

        if(tokenConfirmOptional.isEmpty()){
            data.put("success", false);
            data.put("message", "Token xac thuc tai khoan khong hop le");
            return data;
        }

        TokenConfirm tokenConfirm = tokenConfirmOptional.get();
        if(tokenConfirm.getConfirmedDate() != null){
            data.put("success", false);
            data.put("message", "Token xac thuc tai khoan da duoc xac nhan");
            return data;
        }

        if (tokenConfirm.getExpiryDate().before(new Date())) {
            data.put("success", false);
            data.put("message", "Token xác thực tài khoản đã hết hạn");
            return data;
        }

        User user = tokenConfirm.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        tokenConfirm.setConfirmedDate(new Date());
        tokenConfirmRepository.save(tokenConfirm);

        data.put("success", true);
        data.put("message", "Xác thực tài khoản thành công");
        return data;
    }

    @Override
    public void forgotPassword(String email) {
        log.info("email: {}", email);

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(()-> new ResourceNotFoundException("Email chưa đăng ký dịch vụ."));

        // Create new password and set password of user to new password
        log.info("Create new password");
        String pwd = UUID.randomUUID().toString().substring(0, 8);
        user.setPassword(passwordEncoder.encode(pwd));
        TokenConfirm tokenConfirm = new TokenConfirm();
        tokenConfirm.setToken(pwd);
        tokenConfirm.setUser(user);
        tokenConfirm.setType(TokenType.PASSWORD_RESET);
        // Because password was reissued, not needed set ExpiryDate and ConfirmedDate
        tokenConfirm.setExpiryDate(new Date());
        tokenConfirm.setConfirmedDate(new Date());
        tokenConfirmRepository.save(tokenConfirm);

        // send email
        log.info("Send email");
        Map<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("username", user.getName());
        data.put("pwd", tokenConfirm.getToken());

        mailService.sendMailResetPassword(data);
        log.info("Send mail success");
    }
}
