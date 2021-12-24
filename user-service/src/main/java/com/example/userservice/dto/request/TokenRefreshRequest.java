package com.example.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class TokenRefreshRequest {
    @NotNull
    private String accessToken;
    @NotNull
    private String refreshToken;
}
