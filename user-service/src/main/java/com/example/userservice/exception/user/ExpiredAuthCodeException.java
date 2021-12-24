package com.example.userservice.exception.user;

import com.example.userservice.exception.BaseException;

public class ExpiredAuthCodeException extends BaseException {
    private static final String ERROR_CODE = "signup-003";
    private static final String MESSAGE = "이메일 인증번호의 유효 시간이 만료되었습니다.";

    public ExpiredAuthCodeException() {
        super(ERROR_CODE, MESSAGE);
    }
}
