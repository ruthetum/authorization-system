package com.example.userservice.exception.user;

import com.example.userservice.exception.BaseException;

public class EmptyEmailVerificationException extends BaseException {
    private static final String ERROR_CODE = "sign-002";
    private static final String MESSAGE = "인증 요청 시도가 없는 이메일입니다.";

    public EmptyEmailVerificationException() {
        super(ERROR_CODE, MESSAGE);
    }
}
