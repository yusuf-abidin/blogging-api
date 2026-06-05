package com.neratama.api.verification;

import com.neratama.api.common.exception.BadRequestException;
import com.neratama.api.user.User;
import com.neratama.api.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VerificationService {

    private final OtpService otpService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public VerificationService(OtpService otpService, EmailService emailService, UserRepository userRepository) {
        this.otpService = otpService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    public void sendVerificationOtp(User user) {
        String rawOtp = otpService.generateAndSaveOtp(user);
        emailService.sendOtpEmail(user.getEmail(), user.getFullName(), rawOtp);
    }

    @Transactional
    public void verifyEmail(User user, String rawOtp) {
        if (user.isVerified()) {
            throw new BadRequestException("Email sudah terverifikasi");
        }

        otpService.verifyOtp(user, rawOtp);
        user.setVerified(true);
        userRepository.save(user);
    }
}
