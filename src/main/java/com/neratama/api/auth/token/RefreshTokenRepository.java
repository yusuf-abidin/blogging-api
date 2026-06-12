package com.neratama.api.auth.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenAndIsUsedFalse(String token);

    Optional<RefreshToken> findTopByUserIdAndIsUsedFalseOrderByCreatedAtDesc(Long userId);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.isUsed = true WHERE rt.user.id = :userId")
    void revokeAllByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiresAt < :now OR rt.isUsed = true")
    void deleteExpiredOrUsedTokens(LocalDateTime now);
}
