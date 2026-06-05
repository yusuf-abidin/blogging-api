package com.neratama.api.user;

import com.neratama.api.common.response.ApiResponse;
import com.neratama.api.security.UserPrincipal;
import com.neratama.api.user.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PreAuthorize("hasAuthority('VERIFIED')")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        User currentUser = userPrincipal.getUser();

        UserResponse userResponse = new UserResponse(currentUser);

        ApiResponse<UserResponse> apiResponse = ApiResponse.success("Berhasil mengambil data", userResponse);
        return ResponseEntity.ok(apiResponse);
    }
}
