package com.example.userservice.repository;

import com.example.userservice.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndName(String email, String name);
    long countByStatus(String status);
    Page<User> findAll(Pageable pageable);
}
