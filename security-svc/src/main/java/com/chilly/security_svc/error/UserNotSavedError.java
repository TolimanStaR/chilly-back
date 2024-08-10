package com.chilly.security_svc.error;

public class UserNotSavedError extends RuntimeException {

    public UserNotSavedError(String message) {
        super(message);
    }
}
