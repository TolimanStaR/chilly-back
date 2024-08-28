package com.chilly.security_svc.error;

public class NoUsernameProvidedException extends RuntimeException {
    public NoUsernameProvidedException(String message) {
        super(message);
    }
}
