package com.chilly.security_svc.error;

public class UserAlreadyExitsException extends RuntimeException{
    public UserAlreadyExitsException(String msg) {
        super(msg);
    }
}
