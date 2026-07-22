package com.codeb.mis_invoicing_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "role", nullable = false)
    private String role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.active;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Auth-specific fields (not in original doc, but needed for email verification/reset)
    @Column(nullable = false)
    private boolean emailVerified = false;

    private String verificationToken;
    private String resetToken;
    private LocalDateTime resetTokenExpiry;

    public enum Status {
        active, inactive
    }
}