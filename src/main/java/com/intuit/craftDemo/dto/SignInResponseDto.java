package com.intuit.craftDemo.dto;

public class SignInResponseDto {
    private String status;
    private String token;
    private String message;

    public SignInResponseDto(String status, String token, String message) {
        this.status = status;
        this.token = token;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccess() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
