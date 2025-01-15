package com.chilly.security_svc.error;

public class CannotRecoverPasswordException extends RuntimeException {
    public CannotRecoverPasswordException(String s) {
        super(s);
    }
}
