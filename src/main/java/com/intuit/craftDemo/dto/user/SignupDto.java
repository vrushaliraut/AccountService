package com.intuit.craftDemo.dto.user;

import javax.validation.constraints.NotEmpty;

public class SignupDto {

    @NotEmpty(message = "first_name is required")
    private String first_name;

    private String last_name;

    @NotEmpty(message = "email is required")
    private String email;

    @NotEmpty(message = "password is required")
    private String password;

    public SignupDto(String firstname, String lastname, String email, String password) {
        this.first_name = firstname;
        this.last_name = lastname;
        this.email = email;
        this.password = password;
    }

    public String getFirstname() {
        return first_name;
    }

    public void setFirstname(String firstname) {
        this.first_name = firstname;
    }

    public String getLastname() {
        return last_name;
    }

    public void setLastname(String lastname) {
        this.last_name = lastname;
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
