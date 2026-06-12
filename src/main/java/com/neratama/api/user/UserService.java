package com.neratama.api.user;

import com.neratama.api.auth.token.RefreshTokenService;
import com.neratama.api.common.exception.BadRequestException;
import com.neratama.api.security.jwt.JwtTokenProvider;
import com.neratama.api.user.dto.*;
import com.neratama.api.verification.VerificationService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final VerificationService verificationService;
    private final RefreshTokenService refreshTokenService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, VerificationService verificationService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.verificationService = verificationService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public User registerNewUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email sudah terdaftar");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .provider(AuthProvider.LOCAL)
                .build();

        User savedUser = userRepository.save(user);
        verificationService.sendVerificationOtp(savedUser);

        return savedUser;
    }

    @Transactional
    public AuthResponse loginLocalUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Email atau password salah"));

        if (user.getProvider() != AuthProvider.LOCAL) {
            throw new BadRequestException("Akun ini terdaftar menggunakan Google. Silakan login menggunakan Google");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Email atau password salah");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = refreshTokenService.generateAndSaveToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(new UserResponse(user))
                .build();
    }

    @Transactional
    public UserResponse updateProfile(User user, UpdateProfileRequest request) {
        User freshUser = userRepository.findById(user.getId())
                        .orElseThrow(() -> new BadRequestException("Pengguna tidak ditemukan"));
        freshUser.setFullName(request.getFullName());
        User updatedUser = userRepository.save(freshUser);
        return new UserResponse((updatedUser));
    }

    @Transactional
    public void changePassword(User user, ChangePasswordRequest request) {
        User freshUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new BadRequestException("Pengguna tidak ditemukan"));
        if (freshUser.getProvider() != AuthProvider.LOCAL) {
            throw new BadRequestException("Akun ini terdaftar menggunakan Google. Tidak dapat mengubah password");
        }

        if (!passwordEncoder.matches(request.getOldPassword(), freshUser.getPassword())) {
            throw new BadRequestException("Password lama salah");
        }

        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new BadRequestException("Password baru tidak boleh sama dengan password lama");
        }

        freshUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(freshUser);
    }

    @Transactional
    public AuthResponse refreshAccessToken(String refreshToken) {
        User user = refreshTokenService.validateAndConsumeToken(refreshToken);

        String newAccessToken = jwtTokenProvider.generateAccessToken(user);
        String newRefreshToken = refreshTokenService.generateAndSaveToken(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .user(new UserResponse(user))
                .build();
    }
}
