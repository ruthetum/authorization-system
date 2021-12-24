package com.example.userservice.exception.user;

import com.example.userservice.exception.BaseException;

public class NotValidPasswordException extends BaseException {
    private static final String ERROR_CODE = "signin-001";
    private static final String MESSAGE = "비밀번호가 올바르지 않습니다.";

    public NotValidPasswordException() {
        super(ERROR_CODE, MESSAGE);
    }
}
