package com.intuit.craftDemo.dto.user;

import javax.validation.constraints.NotEmpty;

public class SignInDto {
    @NotEmpty(message = "email is required")
    private String email;

    @NotEmpty(message = "password is required")
    private String password;

    public SignInDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
