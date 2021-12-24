package com.example.userservice.exception.user;

import com.example.userservice.exception.BaseException;

public class EmptyUserException extends BaseException {
    private static final String ERROR_CODE = "user-001";
    private static final String MESSAGE = "존재하지 않는 사용자입니다.";

    public EmptyUserException() {
        super(ERROR_CODE, MESSAGE);
    }
}
