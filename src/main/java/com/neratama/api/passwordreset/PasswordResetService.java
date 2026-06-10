package com.neratama.api.passwordreset;

import com.neratama.api.config.MailProperties;
import com.neratama.api.user.User;
import com.neratama.api.user.UserRepository;
import com.neratama.api.verification.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordResetTokenService tokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final MailProperties mailProperties;

    public PasswordResetService(UserRepository userRepository, PasswordResetTokenService tokenService, EmailService emailService, PasswordEncoder passwordEncoder, MailProperties mailProperties) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.mailProperties = mailProperties;
    }

    @Transactional
    public void requestPasswordReset(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            String rawToken = tokenService.generateAndSaveToken(user);

            String resetUrl = mailProperties.getFrontendUrl() + "/reset-password?token=" + rawToken;

            emailService.sendPasswordResetEmail(user.getEmail(), user.getFullName(), resetUrl);
        });
    }

    @Transactional
    public void resetPassword(String rawToken, String newPassword) {
        User user = tokenService.validateAndConsumeToken(rawToken);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
