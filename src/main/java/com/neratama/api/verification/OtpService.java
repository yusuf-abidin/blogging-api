package com.neratama.api.verification;

import com.neratama.api.common.exception.BadRequestException;
import com.neratama.api.config.MailProperties;
import com.neratama.api.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class OtpService {
    private final EmailVerificationRepository emailVerificationRepository;
    private final MailProperties mailProperties;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();

    public OtpService(EmailVerificationRepository emailVerificationRepository, MailProperties mailProperties, PasswordEncoder passwordEncoder) {
        this.emailVerificationRepository = emailVerificationRepository;
        this.mailProperties = mailProperties;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String generateAndSaveOtp(User user) {
        emailVerificationRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId())
                        .ifPresent(lastOtp -> {
                            long secondsSinceLastOtp = ChronoUnit.SECONDS.between(
                                    lastOtp.getCreatedAt(),
                                    LocalDateTime.now()
                            );

                            long cooldown = mailProperties.getOtpResendCooldownSeconds();

                            if (secondsSinceLastOtp < cooldown) {
                                long remaining = cooldown - secondsSinceLastOtp;
                                throw new BadRequestException("Mohon tunggu " + remaining + " detik sebelum request OTP baru");
                            }
                        });
        emailVerificationRepository.invalidateAllByUserId(user.getId());
        String rawOtp = String.format("%06d", secureRandom.nextInt(999999));
        String hashedOtp = passwordEncoder.encode(rawOtp);

        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(mailProperties.getOtpExpiryMinutes());

        EmailVerification verification = new EmailVerification(user, hashedOtp, expiresAt);

        emailVerificationRepository.save(verification);

        return rawOtp;
    }

    @Transactional
    public void verifyOtp(User user, String rawOtp) {
        EmailVerification verification = emailVerificationRepository.findTopByUserIdAndIsUsedFalseOrderByCreatedAtDesc(user.getId())
                .orElseThrow(() -> new BadRequestException("OTP tidak valid"));

        if (verification.isExpired()) {
            throw new BadRequestException("OTP telah kedaluwarsa");
        }

        if (!passwordEncoder.matches(rawOtp, verification.getOtpCode())) {
            throw new BadRequestException("OTP tidak valid");
        }

        verification.setUsed(true);
        emailVerificationRepository.save(verification);
    }
}
