package com.web.movie.repository;

import com.web.movie.entity.TokenConfirm;
import com.web.movie.entity.enumType.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenConfirmRepository extends JpaRepository<TokenConfirm,Long> {
    Optional<TokenConfirm> findByTokenAndType(String token, TokenType tokenType);
}
