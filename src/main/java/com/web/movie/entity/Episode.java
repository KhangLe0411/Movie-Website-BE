package com.web.movie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "episodes")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "film_id")
    Film film;

    Integer orderEpisode; // Tập 1, tập 2, ...
    String title; // Tập 1: tên tập phim
    Boolean status; // Trạng thái đã phát sóng hay chưa
    Date createdAt;
    Date updatedAt;
    Date publishedAt;

    @OneToOne
    @JoinColumn(name = "video_id")
    Video video;

    @PrePersist
    public void prePersist() {
        createdAt = new Date();
        updatedAt = createdAt;
        if (status) {
            publishedAt = createdAt;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = new Date();
        if (status) {
            publishedAt = updatedAt;
        }
    }
}
