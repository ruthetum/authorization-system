package com.example.apigateway.exception;

public class NotAuthorizationException extends BaseException {
    private static final String ERROR_CODE = "gw-common-002";
    private static final String MESSAGE = "권한이 없습니다.";

    public NotAuthorizationException() {
        super(ERROR_CODE, MESSAGE);
    }
}
