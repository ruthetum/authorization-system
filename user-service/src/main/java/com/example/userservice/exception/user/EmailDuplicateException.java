package com.example.userservice.exception.user;

import com.example.userservice.exception.BaseException;

public class EmailDuplicateException extends BaseException {
    private static final String ERROR_CODE = "mail-001";
    private static final String MESSAGE = "이미 사용 중인 이메일입니다.";

    public EmailDuplicateException() {
        super(ERROR_CODE, MESSAGE);
    }
}
