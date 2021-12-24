package com.example.userservice.dto.response;

import com.example.userservice.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserDetailResponse {
    private String email;
    private String name;
    private LocalDateTime createdAt;

    public UserDetailResponse(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.createdAt = user.getCreatedAt();
    }
}
