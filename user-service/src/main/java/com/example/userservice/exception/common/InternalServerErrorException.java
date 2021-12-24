package com.example.userservice.exception.common;

import com.example.userservice.exception.BaseException;

public class InternalServerErrorException extends BaseException {
    private static final String ERROR_CODE = "common-001";
    private static final String MESSAGE = "서버에 에러가 발생했습니다.";

    public InternalServerErrorException() {
        super(ERROR_CODE, MESSAGE);
    }
}
