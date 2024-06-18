package com.web.movie.repository;

import com.web.movie.entity.Review;
import com.web.movie.payload.dto.ReviewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT new com.web.movie.payload.dto.ReviewDTO(r.id, r.rating, r.comment, r.createdAt, r.updatedAt, new com.web.movie.payload.dto.UserDTO(r.user.id, r.user.name, r.user.email, r.user.phone, r.user.avatar, r.user.role, r.user.enabled)) FROM Review r WHERE r.film.id = ?1 ORDER BY r.createdAt DESC")
    List<ReviewDTO> findByFilm_IdOrderByCreatedAtDesc(Integer id);
}
