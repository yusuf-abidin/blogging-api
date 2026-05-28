package com.neratama.api.user;

import com.neratama.api.common.exception.BadRequestException;
import com.neratama.api.user.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Unit Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterRequest baseRequest;

    @BeforeEach
    void setUp() {
        baseRequest = new RegisterRequest();
        baseRequest.setFullName("Yusuf Abidin Nurrahman");
        baseRequest.setEmail("yusuf@example.com");
        baseRequest.setPassword("securePassword123");
    }

    @Nested
    @DisplayName("Registration Scenarios")
    class RegistrationScenarios {

        @Test
        @DisplayName("Should successfully register user when input is valid and email is unique")
        void shouldRegisterUser_WhenRequestIsValidAndEmailIsUnique() {
            // Given
            when(userRepository.existsByEmail(baseRequest.getEmail())).thenReturn(false);
            when(passwordEncoder.encode(baseRequest.getPassword())).thenReturn("hashed_password_xyz");

            User mockSavedUser = User.builder()
                    .id(99L)
                    .fullName(baseRequest.getFullName())
                    .email(baseRequest.getEmail())
                    .password("hashed_password_xyz")
                    .role(Role.USER)
                    .provider(AuthProvider.LOCAL)
                    .build();

            when(userRepository.save(any(User.class))).thenReturn(mockSavedUser);

            // When
            User result = userService.registerNewUser(baseRequest);

            // Then
            assertNotNull(result);
            assertEquals(99L, result.getId());
            assertEquals(baseRequest.getEmail(), result.getEmail());
            assertEquals("hashed_password_xyz", result.getPassword());
            assertEquals(Role.USER, result.getRole());
            assertEquals(AuthProvider.LOCAL, result.getProvider());

            verify(userRepository).existsByEmail(baseRequest.getEmail());
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw BadRequestException when email is already registered")
        void shouldThrowException_WhenEmailAlreadyExists() {
            // Given
            when(userRepository.existsByEmail(baseRequest.getEmail())).thenReturn(true);

            // When & Then
            BadRequestException exception = assertThrows(BadRequestException.class, () ->
                    userService.registerNewUser(baseRequest)
            );

            assertEquals("Email sudah terdaftar", exception.getMessage());
            verify(userRepository, never()).save(any(User.class));
        }

        @ParameterizedTest(name = "Should work properly with email variation: {0}")
        @CsvSource({
                "user.antara@domain.com",
                "yusuf+alias@gmail.com",
                "admin.neratama@neratama.id"
        })
        @DisplayName("Should successfully handle various valid email formats")
        void shouldRegisterUser_WithEmailVariations(String emailInput) {
            // Given
            baseRequest.setEmail(emailInput);
            when(userRepository.existsByEmail(emailInput)).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("hashed_pass");

            User mockUser = User.builder()
                    .id(1L)
                    .email(emailInput)
                    .role(Role.USER)
                    .build();
            when(userRepository.save(any(User.class))).thenReturn(mockUser);

            // When
            User result = userService.registerNewUser(baseRequest);

            // Then
            assertEquals(emailInput, result.getEmail());
            verify(userRepository).save(any(User.class));
        }
    }
}