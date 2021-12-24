package com.example.userservice.repository;

import com.example.userservice.domain.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findTop1ByEmailOrderByCreatedAtDesc(String email);
}
