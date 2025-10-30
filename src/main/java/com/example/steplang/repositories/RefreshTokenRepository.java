package com.example.steplang.repositories;

import com.example.steplang.entities.RefreshToken;
import com.example.steplang.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken r WHERE r.user = :user")
    int deleteByUser(@Param("user") User user);
    void deleteByTokenAndUser(String token, User user);
    void deleteByExpirationDateBefore(Instant now);
}
