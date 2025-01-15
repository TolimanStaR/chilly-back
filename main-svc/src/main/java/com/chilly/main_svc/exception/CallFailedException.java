package com.chilly.main_svc.exception;

public class CallFailedException extends RuntimeException {
    public CallFailedException(String s) {
        super(s);
    }
}
