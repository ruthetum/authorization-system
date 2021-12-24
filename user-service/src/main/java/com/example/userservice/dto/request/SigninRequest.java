package com.example.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class SigninRequest {

    @NotNull(message = "email is null")
    @Size(min = 2, max = 255, message = "email at least 2 letters And maximum of 255 letters")
    @Email
    private String email;

    @NotNull(message = "password is null")
    @Size(min = 8, max = 20, message = "password at least 8 letters And maximum of 20 letters")
    private String password;
}
