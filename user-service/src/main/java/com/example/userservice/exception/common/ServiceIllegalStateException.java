package com.example.userservice.exception.common;

import com.example.userservice.exception.BaseException;

public class ServiceIllegalStateException extends BaseException {
    private static final String ERROR_CODE = "common-003";
    private static final String MESSAGE = "잘못된 요청입니다.";

    public ServiceIllegalStateException() {
        super(ERROR_CODE, MESSAGE);
    }
}
