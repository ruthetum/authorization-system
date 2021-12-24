package com.example.apigateway.exception;

public class ExpiredTokenException extends BaseException {
    private static final String ERROR_CODE = "gw-common-001";
    private static final String MESSAGE = "토큰이 만료되었습니다.";

    public ExpiredTokenException() {
        super(ERROR_CODE, MESSAGE);
    }
}
