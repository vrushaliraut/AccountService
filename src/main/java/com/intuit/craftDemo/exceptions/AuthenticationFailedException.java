package com.intuit.craftDemo.exceptions;

public class AuthenticationFailedException extends Throwable {
    public AuthenticationFailedException(String authTokenNotPresent) {
        super(authTokenNotPresent);
    }
}
