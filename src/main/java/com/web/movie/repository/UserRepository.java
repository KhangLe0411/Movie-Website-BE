package com.web.movie.repository;

import com.web.movie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // create free method like save, findById etc
    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByPhoneIgnoreCase(String phone);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByIdAndEmailIgnoreCase(Long id, String email);
}
