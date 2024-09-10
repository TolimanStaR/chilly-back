package com.chilly.security_svc.error;

public class NoDataProvidedException extends RuntimeException {
    public NoDataProvidedException(String message) {
        super(message);
    }
}
