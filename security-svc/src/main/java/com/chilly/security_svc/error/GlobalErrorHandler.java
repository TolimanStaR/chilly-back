package com.chilly.security_svc.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(UserNotSavedError.class)
    public ResponseEntity<ErrorResponse> handleNotSavedError(UserNotSavedError error) {
        ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), error.getMessage());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
