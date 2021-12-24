package com.example.userservice.exception.user;

import com.example.userservice.exception.BaseException;

public class NonValidRefreshToken extends BaseException {
    private static final String ERROR_CODE = "token-001";
    private static final String MESSAGE = "Refresh token이 유효하지 않습니다.";

    public NonValidRefreshToken() {
        super(ERROR_CODE, MESSAGE);
    }
}
