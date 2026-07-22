package com.codeb.mis_invoicing_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.backend.url}")
    private String backendUrl;

    public void sendVerificationEmail(String toEmail, String token) {
        String verificationLink = backendUrl + "/api/auth/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("CodeB - Verify your email");
        message.setText("Please click the link below to verify your email:\n\n" + verificationLink);

        mailSender.send(message);
    }

    public void sendResetPasswordEmail(String toEmail, String token) {
        String resetLink = backendUrl + "/api/auth/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("CodeB - Reset your password");
        message.setText("Click the link below to reset your password (valid for 15 minutes):\n\n" + resetLink);

        mailSender.send(message);
    }
}