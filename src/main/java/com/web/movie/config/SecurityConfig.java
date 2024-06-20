package com.web.movie.config;

import com.web.movie.security.CustomFilter;
import com.web.movie.service.implement.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final CustomFilter customFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(c->c.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();

                    corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
                    corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                    corsConfiguration.setAllowCredentials(true);
                    corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                    corsConfiguration.setExposedHeaders(List.of("Authorization"));
                    corsConfiguration.setMaxAge(3600L);

                    return corsConfiguration;
                }))
                .securityMatcher("/api/**")
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/css/**", "/js/**", "/image/**", "/admin-assets/**", "/image_uploads/**").permitAll()
                        .requestMatchers("/", "/phim-bo", "/phim-chieu-rap", "/phim-le", "/tin-tuc", "/tin-tuc/**", "/phim/**", "/xem-phim/**", "/cua-hang", "/cua-hang/**", "/dang-nhap", "/dang-ky", "/quen-mat-khau", "/xac-thuc-tai-khoan", "/dat-lai-mat-khau", "/quoc-gia/**", "/the-loai/**").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        // User
                        .requestMatchers("/api/v1/users/**").permitAll()
                        .requestMatchers("/api/v1/home/**").permitAll()
                        .requestMatchers("/api/v1/film/**").permitAll()
                        .requestMatchers("/api/v1/genre/**").permitAll()
                        .requestMatchers("/api/v1/actor/**").permitAll()
                        .requestMatchers("/api/v1/country/**").permitAll()
                        .requestMatchers("/api/v1/review/**").permitAll()
                        // Admin
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                );
        // If request No-Auth return 401 instead 403.
        http.exceptionHandling(customize -> customize.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        http.authenticationProvider(authenticationProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class); // jwt token Filter is added before id/password Filter

        return http.build();
    }
}
