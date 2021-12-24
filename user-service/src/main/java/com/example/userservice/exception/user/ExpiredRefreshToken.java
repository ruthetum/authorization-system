package com.example.userservice.exception.user;

import com.example.userservice.exception.BaseException;

public class ExpiredRefreshToken extends BaseException {
    private static final String ERROR_CODE = "token-002";
    private static final String MESSAGE = "Refresh tokend이 만료되었습니다.";

    public ExpiredRefreshToken() {
        super(ERROR_CODE, MESSAGE);
    }
}