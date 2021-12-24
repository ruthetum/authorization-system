package com.example.userservice.exception;

import com.example.userservice.dto.response.ErrorResponse;
import com.example.userservice.exception.common.BadRequestException;
import com.example.userservice.exception.common.InternalServerErrorException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleException(
            BaseException e
    ) {
        log.error("code : {}, message : {}",
                e.getErrorCode(), e.getMessage());
        return ResponseEntity.badRequest().body(e.toResponse());
    }

    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentNotValidException.class,
            InvalidFormatException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(
            Exception e, HttpServletRequest request
    ) {
        log.error("url : {}, message : {}",
                request.getRequestURI(), e.getMessage());

        return ResponseEntity.badRequest().body(new BadRequestException().toResponse());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception e, HttpServletRequest request
    ) {
        log.error("url : {}, message : {}",
                request.getRequestURI(), e.getClass());
        e.printStackTrace();

        return ResponseEntity.badRequest().body(new InternalServerErrorException().toResponse());
    }
}
