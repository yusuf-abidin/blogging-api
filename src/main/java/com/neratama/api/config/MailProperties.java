package com.neratama.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.mail")
public class MailProperties {

    private String from;
    private int otpExpiryMinutes;
    private int otpResendCooldownSeconds;
    private int passwordResetExpiryMinutes;
    private int passwordResetCooldownSeconds;
    private String frontendUrl;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getOtpExpiryMinutes() {
        return otpExpiryMinutes;
    }

    public void setOtpExpiryMinutes(int otpExpiryMinutes) {
        this.otpExpiryMinutes = otpExpiryMinutes;
    }

    public int getOtpResendCooldownSeconds() {
        return otpResendCooldownSeconds;
    }

    public void setOtpResendCooldownSeconds(int otpResendCooldownSeconds) {
        this.otpResendCooldownSeconds = otpResendCooldownSeconds;
    }

    public int getPasswordResetExpiryMinutes() {
        return passwordResetExpiryMinutes;
    }

    public void setPasswordResetExpiryMinutes(int passwordResetExpiryMinutes) {
        this.passwordResetExpiryMinutes = passwordResetExpiryMinutes;
    }

    public int getPasswordResetCooldownSeconds() {
        return passwordResetCooldownSeconds;
    }

    public void setPasswordResetCooldownSeconds(int passwordResetCooldownSeconds) {
        this.passwordResetCooldownSeconds = passwordResetCooldownSeconds;
    }

    public String getFrontendUrl() {
        return frontendUrl;
    }

    public void setFrontendUrl(String frontendUrl) {
        this.frontendUrl = frontendUrl;
    }
}
