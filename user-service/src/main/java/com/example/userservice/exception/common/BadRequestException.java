package com.example.userservice.exception.common;

import com.example.userservice.exception.BaseException;

public class BadRequestException extends BaseException {
    private static final String ERROR_CODE = "common-005";
    private static final String MESSAGE = "잘못된 요청입니다.";

    public BadRequestException() {
        super(ERROR_CODE, MESSAGE);
    }
}
