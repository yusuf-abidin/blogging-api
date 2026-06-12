package com.neratama.api.auth.token;

import com.neratama.api.common.exception.BadRequestException;
import com.neratama.api.security.jwt.JwtProperties;
import com.neratama.api.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtProperties jwtProperties, PasswordEncoder passwordEncoder) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtProperties = jwtProperties;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String generateAndSaveToken(User user) {
        refreshTokenRepository.revokeAllByUserId(user.getId());

        byte[] rawBytes = new byte[32];
        secureRandom.nextBytes(rawBytes);
        String randomPart = Base64.getUrlEncoder().withoutPadding().encodeToString(rawBytes);
        String rawToken = user.getId() + "." + randomPart;

        String hashedToken = passwordEncoder.encode(rawToken);

        long expiryMs = jwtProperties.getRefreshExpirationMs();
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(expiryMs / 1000);

        refreshTokenRepository.save(new RefreshToken(user, hashedToken, expiresAt));

        return rawToken;
    }

    @Transactional
    public User validateAndConsumeToken(String rawToken) {
        String[] parts = rawToken.split("\\.", 2);
        if (parts.length != 2) {
            throw new BadRequestException("Refresh token tidak valid");
        }

        Long userId;
        try {
            userId = Long.parseLong(parts[0]);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Refresh token tidak valid");
        }

        RefreshToken refreshToken = refreshTokenRepository.findTopByUserIdAndIsUsedFalseOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new BadRequestException("Refresh token tidak valid"));

        if (refreshToken.isExpired()) {
            throw new BadRequestException("Refresh token tidak valid");
        }

        if (!passwordEncoder.matches(rawToken, refreshToken.getToken())) {
            throw new BadRequestException("Refresh token tidak valid");
        }

        refreshToken.setUsed(true);
        refreshTokenRepository.save(refreshToken);

        return refreshToken.getUser();
    }
}
