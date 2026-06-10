package com.neratama.api.user;

import com.neratama.api.common.response.ApiResponse;
import com.neratama.api.security.UserPrincipal;
import com.neratama.api.user.dto.ChangePasswordRequest;
import com.neratama.api.user.dto.UpdateProfileRequest;
import com.neratama.api.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('VERIFIED')")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        User currentUser = userPrincipal.getUser();

        UserResponse userResponse = new UserResponse(currentUser);

        ApiResponse<UserResponse> apiResponse = ApiResponse.success("Berhasil mengambil data", userResponse);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAuthority('VERIFIED')")
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserResponse updatedUser = userService.updateProfile(userPrincipal.getUser(), request);
        return ResponseEntity.ok(ApiResponse.success("Profil berhasil diperbarui", updatedUser));
    }

    @PreAuthorize("hasAuthority('VERIFIED')")
    @PutMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        userService.changePassword(userPrincipal.getUser(), request);
        return ResponseEntity.ok(ApiResponse.success("Password berhasil diubah", null));
    }
}
