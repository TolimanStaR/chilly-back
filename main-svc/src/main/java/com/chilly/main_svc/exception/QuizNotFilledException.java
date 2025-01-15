package com.chilly.main_svc.exception;

public class QuizNotFilledException extends RuntimeException {
    public QuizNotFilledException(String s) {
        super(s);
    }
}
