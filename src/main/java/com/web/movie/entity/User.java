package com.web.movie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.movie.entity.enumType.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "users")
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String name;

    @Column(unique = true)
    String email;

    String phone;

    @JsonIgnore
    @Column(nullable = false)
    String password;

    String avatar;

    @Enumerated(EnumType.STRING)
    UserRole role;

    private Boolean enabled;

    Date createdAt;
    Date updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = new Date();
        updatedAt = createdAt;

        if (role == null) {
            role = UserRole.USER;
        }

        if (enabled == null) {
            enabled = false;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = new Date();
    }
}
