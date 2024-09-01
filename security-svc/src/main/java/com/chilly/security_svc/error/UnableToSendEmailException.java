package com.chilly.security_svc.error;

public class UnableToSendEmailException extends RuntimeException {
    public UnableToSendEmailException(String s) {
        super(s);
    }
}
