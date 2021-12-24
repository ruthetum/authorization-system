package com.example.userservice.exception.user;

import com.example.userservice.exception.BaseException;

public class NotValidEmailAuthCodeException extends BaseException {
    private static final String ERROR_CODE = "signup-004";
    private static final String MESSAGE = "인증번호가 올바르지 않습니다.";

    public NotValidEmailAuthCodeException() {
        super(ERROR_CODE, MESSAGE);
    }
}
