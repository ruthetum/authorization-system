package com.example.userservice.exception.user;

import com.example.userservice.exception.BaseException;

public class NotCorrespondPasswordCheckException extends BaseException {
    private static final String ERROR_CODE = "signup-001";
    private static final String MESSAGE = "비밀번호를 다시 확인해주세요.";

    public NotCorrespondPasswordCheckException() {
        super(ERROR_CODE, MESSAGE);
    }
}
