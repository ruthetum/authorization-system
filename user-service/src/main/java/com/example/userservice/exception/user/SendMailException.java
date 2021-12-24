package com.example.userservice.exception.user;

import com.example.userservice.exception.BaseException;

public class SendMailException extends BaseException {
    private static final String ERROR_CODE = "mail-002";
    private static final String MESSAGE = "메일 전송 중 에러가 발생했습니다.";

    public SendMailException() {
        super(ERROR_CODE, MESSAGE);
    }
}
