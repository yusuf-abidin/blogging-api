package com.neratama.api.verification;

import com.neratama.api.config.MailProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    private final JavaMailSender mailsender;
    private final TemplateEngine templateEngine;
    private final MailProperties mailProperties;

    public EmailService(JavaMailSender mailsender, TemplateEngine templateEngine, MailProperties mailProperties) {
        this.mailsender = mailsender;
        this.templateEngine = templateEngine;
        this.mailProperties = mailProperties;
    }

    @Async
    public void sendOtpEmail(String toEmail, String fullName, String otp) {
        try {
            Context context = new Context();
            context.setVariable("fullName", fullName);
            context.setVariable("otp", otp);
            context.setVariable("expiryMinutes", mailProperties.getOtpExpiryMinutes());

            String htmlContent = templateEngine.process("email/otp-verification.html", context);

            MimeMessage message = mailsender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(mailProperties.getFrom());
            helper.setTo(toEmail);
            helper.setSubject("Verifikasi Email - Neratama");
            helper.setText(htmlContent, true);

            mailsender.send(message);
        }catch (MessagingException e) {
            throw new RuntimeException("Gagal mengirim email OTP: " + e);
        }
    }
}
