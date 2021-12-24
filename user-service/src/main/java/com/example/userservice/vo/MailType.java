package com.example.userservice.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MailType {
    VERIFY("이메일 인증"),
    PASSWORD_CLEAR("비밀번호 초기화");

    private final String description;
}
