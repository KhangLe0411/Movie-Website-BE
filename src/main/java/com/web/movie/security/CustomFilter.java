package com.web.movie.security;

import com.web.movie.service.implement.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtilities jwtUtilities;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        // Danh sách các đường dẫn permitAll
        List<String> permitAllPaths = Arrays.asList("/css/", "/js/", "/image/");

        // Kiểm tra xem requestURI có nằm trong danh sách permitAll không
        boolean isPermitAllPath = permitAllPaths.stream().anyMatch(requestURI::startsWith);

        if (isPermitAllPath) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            List<String> allowedPaths = Arrays.asList("/api/v1/auth", "/api/v1/home", "/api/v1/film", "/api/v1/genre",
                    "/api/v1/country", "/api/v1/actor", "/api/v1/director");
            if(allowedPaths.stream().anyMatch(request.getServletPath()::contains)){
                filterChain.doFilter(request, response);
                return;
            }

            String authHeader = request.getHeader("Authorization");
            if(!authHeader.startsWith("Bearer ") || authHeader == null){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            String token = jwtUtilities.getToken(request);
            System.out.println("doFilterInternal call validateToken method - Token valid = " + jwtUtilities.validateToken(token));

            if (token != null && jwtUtilities.validateToken(token)) {

                String email = jwtUtilities.extractUsername(token);

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                System.out.println(userDetails.getAuthorities());

                if (userDetails != null && jwtUtilities.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    System.out.println("Authentication filter: " + authentication);
                    log.info("authenticated user with email: {}", email);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                else {
                    System.out.println("Token expired");
                }
            }
            else {
                System.out.println("Token is not valid");
            }
        }catch (Exception e){
            System.out.println("Cannot set user authentication: " + e);
        }

        filterChain.doFilter(request, response);
    }
}
