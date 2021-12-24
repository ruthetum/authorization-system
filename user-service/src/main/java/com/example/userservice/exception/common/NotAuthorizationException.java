package com.example.userservice.exception.common;

import com.example.userservice.exception.BaseException;

public class NotAuthorizationException extends BaseException {
    private static final String ERROR_CODE = "common-004";
    private static final String MESSAGE = "권한이 없습니다.";

    public NotAuthorizationException() {
        super(ERROR_CODE, MESSAGE);
    }
}
