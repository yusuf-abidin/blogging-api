package com.neratama.api.passwordreset;

import com.neratama.api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findTopByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<PasswordResetToken> findTopByUserIdAndIsUsedFalseOrderByCreatedAtDesc(Long userId);

    @Modifying
    @Query("UPDATE PasswordResetToken t SET t.isUsed = true WHERE t.user.id = :userId")
    void invalidateAllByUserId(Long userId);

    Long user(User user);
}
