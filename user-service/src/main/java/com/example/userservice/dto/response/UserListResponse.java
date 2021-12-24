package com.example.userservice.dto.response;

import com.example.userservice.domain.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class UserListResponse {
    private final Long count;
    private final List<UserInfo> users;

    public UserListResponse(Long count, List<UserInfo> users) {
        this.count = count;
        this.users = users;
    }

    @Getter
    public static class UserInfo {
        private String email;
        private String name;
        private LocalDateTime createdAt;
        private String role;

        public UserInfo(User user) {
            this.email = user.getEmail();
            this.name = user.getName();
            this.createdAt = user.getCreatedAt();
            this.role = user.getRole();
        }
    }
}
