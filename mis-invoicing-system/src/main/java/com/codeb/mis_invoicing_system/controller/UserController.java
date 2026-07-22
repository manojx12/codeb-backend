package com.codeb.mis_invoicing_system.controller;

import com.codeb.mis_invoicing_system.config.JwtUtil;
import com.codeb.mis_invoicing_system.entity.User;
import com.codeb.mis_invoicing_system.repository.UserRepository;
import com.codeb.mis_invoicing_system.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setEmailVerified(false);

        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), token);

        return ResponseEntity.ok("Registration successful. Please check your email to verify your account.");
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        Optional<User> userOptional = userRepository.findByVerificationToken(token);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(400).body("Invalid or expired verification link");
        }

        User user = userOptional.get();
        user.setEmailVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);

        return ResponseEntity.ok("Email verified successfully! You can now log in.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {

        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        User user = userOptional.get();

        boolean passwordMatches = passwordEncoder.matches(loginRequest.getPasswordHash(), user.getPasswordHash());

        if (!passwordMatches) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        if (!user.isEmailVerified()) {
            return ResponseEntity.status(403).body("Please verify your email before logging in");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return ResponseEntity.ok(Map.of(
                "token", token,
                "name", user.getFullName(),
                "role", user.getRole()
        ));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body("No account found with this email");
        }

        User user = userOptional.get();

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(java.time.LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        emailService.sendResetPasswordEmail(user.getEmail(), token);

        return ResponseEntity.ok("Password reset link sent to your email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody Map<String, String> request) {
        Optional<User> userOptional = userRepository.findByResetToken(token);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(400).body("Invalid or expired reset link");
        }

        User user = userOptional.get();

        if (user.getResetTokenExpiry().isBefore(java.time.LocalDateTime.now())) {
            return ResponseEntity.status(400).body("Reset link has expired. Please request a new one.");
        }

        String newPassword = request.get("newPassword");
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successfully. You can now log in with your new password.");
    }

}