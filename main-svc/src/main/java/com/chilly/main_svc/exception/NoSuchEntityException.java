package com.chilly.main_svc.exception;

public class NoSuchEntityException extends RuntimeException {
    public NoSuchEntityException(String s) {
        super(s);
    }
}
