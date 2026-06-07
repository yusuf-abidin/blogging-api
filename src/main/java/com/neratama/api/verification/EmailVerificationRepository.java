package com.neratama.api.verification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findTopByUserIdAndIsUsedFalseOrderByCreatedAtDesc(Long userId);

    @Modifying
    @Query("UPDATE EmailVerification ev SET ev.isUsed = true WHERE ev.user.id = :userId")
    void invalidateAllByUserId(Long userId);

    Optional<EmailVerification> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
