package com.example.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerifyRequest {

    @NotNull(message = "email is null")
    @Size(min = 2, max = 255, message = "email at least 2 letters And maximum of 255 letters")
    @Email
    private String email;
}
