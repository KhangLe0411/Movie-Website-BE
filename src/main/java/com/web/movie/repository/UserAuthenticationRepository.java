package com.web.movie.repository;

import com.web.movie.entity.User;
import com.web.movie.entity.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthenticationRepository extends JpaRepository<UserAuthentication, Long> {
    UserAuthentication findByUser(User user);

    UserAuthentication findByUser_Email(String email);

    UserAuthentication findByToken(String token);

    boolean existsByUser(User user);
}
