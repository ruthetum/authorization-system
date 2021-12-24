package com.example.userservice.domain.type.userStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
    NORMAL("정상 회원", "NORMAL"),
    SUSPENDED("정지 회원", "SUSPENDED"),
    RETIRED("탈퇴 회원", "RETIRED");

    private final String description;
    private final String word;
}
