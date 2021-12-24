package com.example.userservice.controller;

import com.example.userservice.dto.request.*;
import com.example.userservice.dto.response.SigninResponse;
import com.example.userservice.dto.response.UserDetailResponse;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.userservice.config.jwt.JwtUtils.ACCESS_TOKEN_HEADER_NAME;
import static com.example.userservice.config.jwt.JwtUtils.getEmailByToken;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-service")
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * updated 21.12.17.
     * 1. 인증 코드 전송
     */
    @PostMapping("/verification")
    public ResponseEntity<Void> sendAuthCodeByEmail(
            @Valid @RequestBody EmailVerifyRequest request
    ) {
        log.info("POST /verification/email");
        userService.sendAuthCodeByEmail(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * updated 21.12.16.
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        log.info("POST /signup");
        userService.signup(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * update 21.12.17.
     * 로그인
     */
    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> signin(
            @Valid @RequestBody SigninRequest request
    ) {
        log.info("POST /signin");
        SigninResponse signinResponse = userService.signin(request);
        return new ResponseEntity<>(signinResponse, HttpStatus.OK);
    }

    /**
     * updated 21.12.17.
     * 비밀번호 초기화 (메일로 전송)
     */
    @PutMapping("/users/password")
    public ResponseEntity<Void> clearPassword(
            @Valid @RequestBody ClearPasswordRequest request
    ) {
        log.info("POST /users/clear");
        userService.clearPassword(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 토큰 재발급
     */
    @PostMapping("/token")
    public ResponseEntity<SigninResponse> tokenRefresh(
            @Valid @RequestBody TokenRefreshRequest request
    ) {
        log.info("POST /users/token");
        SigninResponse signinResponse = userService.tokenRefresh(request);
        return new ResponseEntity<>(signinResponse, HttpStatus.OK);
    }

    /**
     * 사용자 정보 확인
     */
    @GetMapping("/api/users/{userId}")
    public ResponseEntity<UserDetailResponse> getUserDetail(
            @PathVariable Long userId
    ) {
        log.info("GET users/{}", userId);
        return new ResponseEntity<>(
                userService.getUserDetail(userId),
                HttpStatus.OK
        );
    }

    @GetMapping("/api/users/my")
    public ResponseEntity<UserDetailResponse> getMyInfo(
            @RequestHeader(ACCESS_TOKEN_HEADER_NAME) String token
    ) {
        log.info("GET users");
        String email = getEmailByToken(token);
        return new ResponseEntity<>(
                userService.getUserDetailByEmail(email),
                HttpStatus.OK
        );
    }
}
