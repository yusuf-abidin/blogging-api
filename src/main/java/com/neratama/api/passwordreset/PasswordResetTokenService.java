package com.neratama.api.passwordreset;

import com.neratama.api.common.exception.BadRequestException;
import com.neratama.api.config.MailProperties;
import com.neratama.api.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository tokenRepository;
    private final MailProperties mailProperties;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();


    public PasswordResetTokenService(PasswordResetTokenRepository tokenRepository, MailProperties mailProperties, PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.mailProperties = mailProperties;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String generateAndSaveToken(User user) {
        tokenRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId())
                .ifPresent(lastToken -> {
                    long secondsSinceLast = ChronoUnit.SECONDS.between(
                            lastToken.getCreatedAt(),
                            LocalDateTime.now()
                    );

                    long cooldown = mailProperties.getPasswordResetCooldownSeconds();

                    if (secondsSinceLast < cooldown) {
                        long remaining = cooldown - secondsSinceLast;
                        throw new BadRequestException("Mohon tunggu " + remaining + " detik sebelum request reset password baru");
                    }
                });

        tokenRepository.invalidateAllByUserId(user.getId());

        byte[] rawBytes = new byte[32];
        secureRandom.nextBytes(rawBytes);
        String randomPart = Base64.getUrlEncoder().withoutPadding().encodeToString(rawBytes);
        String rawToken = user.getId() + "." + randomPart;

        String hashedToken = passwordEncoder.encode(rawToken);

        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(mailProperties.getPasswordResetExpiryMinutes());

        tokenRepository.save(new PasswordResetToken(user, hashedToken, expiresAt));

        return rawToken;
    }

    @Transactional
    public User validateAndConsumeToken(String rawToken) {
        String[] parts = rawToken.split("\\.");
        if (parts.length != 2) {
            throw new BadRequestException("Token tidak valid");
        }

        Long userId;
        try {
            userId = Long.parseLong(parts[0]);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Token tidak valid");
        }

        PasswordResetToken resetToken = tokenRepository
                .findTopByUserIdAndIsUsedFalseOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new BadRequestException("Token tidak valid"));

        if (resetToken.isExpired()) {
            throw new BadRequestException("Token sudah kadaluarsa");
        }

        if (!passwordEncoder.matches(rawToken, resetToken.getToken())) {
            throw new BadRequestException("Token tidak valid");
        }

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
        return resetToken.getUser();
    }
}
