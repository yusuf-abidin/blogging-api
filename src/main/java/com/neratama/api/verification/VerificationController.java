package com.neratama.api.verification;

import com.neratama.api.common.response.ApiResponse;
import com.neratama.api.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class VerificationController {

    private final VerificationService verificationService;


    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<Void>> resendOtp(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        verificationService.sendVerificationOtp(userPrincipal.getUser());
        return ResponseEntity.ok(ApiResponse.success("OTP verifikasi berhasil dikirim ulang", null));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(
            @Valid @RequestBody VerifyOtpRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        verificationService.verifyEmail(userPrincipal.getUser(), request.getOtp());
        return ResponseEntity.ok(ApiResponse.success("Email berhasil diverifikasi", null));

    }
}
