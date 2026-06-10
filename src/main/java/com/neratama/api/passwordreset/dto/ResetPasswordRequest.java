package com.neratama.api.passwordreset.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {
    @NotBlank(message = "Token tidak boleh kosong")
    private String token;

    @NotBlank(message = "Password baru tidak boleh kosong")
    @Size(min = 6, max = 255, message = "Password minimal 6 karakter")
    private String newPassword;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
