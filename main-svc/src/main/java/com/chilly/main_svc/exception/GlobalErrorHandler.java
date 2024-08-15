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



    private ResponseEntity<ErrorResponse> wrapException(HttpStatus status, Exception exception) {
        ErrorResponse response = new ErrorResponse(status.value(), exception.getMessage());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
