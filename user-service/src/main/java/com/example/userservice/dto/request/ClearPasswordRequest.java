package com.example.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClearPasswordRequest {

    @NotNull(message = "email is null")
    @Size(min = 2, max = 255, message = "email at least 2 letters And maximum of 255 letters")
    @Email
    private String email;

    @NotNull(message = "name is null")
    @Size(min = 2, max = 20, message = "name at least 2 letters And maximum of 20 letters")
    private String name;
}
