package com.neratama.api.verification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VerifyOtpRequest {

    @NotBlank(message = "OTP tidak boleh kosong")
    @Size(min = 6, max = 6, message = "OTP harus terdiri dari 6 digit")
    private String otp;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
