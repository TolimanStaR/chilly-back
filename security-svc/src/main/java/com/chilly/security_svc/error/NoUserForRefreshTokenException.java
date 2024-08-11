package com.chilly.security_svc.error;

public class NoUserForRefreshTokenException extends RuntimeException {
    public NoUserForRefreshTokenException(String message) {
        super(message);
    }
}
