package com.example.userservice.domain.type.role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN("관리자", "ADMIN"),
    PERSONAL("개인 회원", "PERSONAL");

    private final String description;
    private final String word;
}
