package com.chilly.main_svc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotSavedError(RuntimeException exception) {
        return wrapException(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(CallFailedException.class)
    public ResponseEntity<ErrorResponse> handleNotSavedError(CallFailedException exception) {
        return wrapException(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(QuizNotFilledException.class)
    public ResponseEntity<ErrorResponse> handleNotSavedError(QuizNotFilledException exception) {
        return wrapException(HttpStatus.NOT_ACCEPTABLE, exception);
    }

    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity<ErrorResponse> handleNotSavedError(NoSuchEntityException exception) {
        return wrapException(HttpStatus.BAD_REQUEST, exception);
    }

    private ResponseEntity<ErrorResponse> wrapException(HttpStatus status, Exception exception) {
        ErrorResponse response = new ErrorResponse(status.value(), exception.getMessage());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
