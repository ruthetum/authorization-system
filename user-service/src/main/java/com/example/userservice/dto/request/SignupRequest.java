package com.example.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @NotNull(message = "email is null")
    @Size(min = 2, max = 255, message = "email at least 2 letters And maximum of 255 letters")
    @Email
    private String email;

    @NotNull(message = "email auth code is null")
    @Size(min = 6, max = 6, message = "email auth code is length 6")
    private String emailAuthCode;

    @NotNull(message = "password is null")
    @Size(min = 8, max = 20, message = "password at least 8 letters And maximum of 20 letters")
    private String password;

    @NotNull(message = "password check is null")
    @Size(min = 8, max = 20, message = "password check at least 8 letters And maximum of 20 letters")
    private String passwordCheck;

    @NotNull(message = "name is null")
    @Size(min = 2, max = 20, message = "name at least 2 letters And maximum of 20 letters")
    private String name;
}
