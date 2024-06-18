package com.web.movie.service.implement;

import com.web.movie.entity.UserAuthentication;
import com.web.movie.repository.UserAuthenticationRepository;
import com.web.movie.security.JwtUtilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final UserAuthenticationRepository userAuthenticationRepository;

    private final JwtUtilities jwtUtilities;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String token = jwtUtilities.getToken(request);

        if (!jwtUtilities.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        UserAuthentication storedToken = userAuthenticationRepository.findByToken(token);

        if (storedToken == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            storedToken.setToken(null);
            storedToken.setExpiredAt(new Date());

            userAuthenticationRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}
