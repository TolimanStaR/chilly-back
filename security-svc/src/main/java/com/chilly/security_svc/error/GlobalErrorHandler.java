package com.chilly.security_svc.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(UserNotSavedError.class)
    public ResponseEntity<ErrorResponse> handleNotSavedError(UserNotSavedError error) {
        return wrapException(HttpStatus.INTERNAL_SERVER_ERROR, error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotSavedError(UserNotFoundException error) {
        return wrapException(HttpStatus.NOT_FOUND, error);
    }

    @ExceptionHandler(NoUserForRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleNotSavedError(NoUserForRefreshTokenException error) {
        return wrapException(HttpStatus.UNAUTHORIZED, error);
    }

    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleNotSavedError(ExpiredRefreshTokenException error) {
        return wrapException(HttpStatus.UNAUTHORIZED, error);
    }

    @ExceptionHandler(NoUsernameProvidedException.class)
    public ResponseEntity<ErrorResponse> handleNoUsernameProvided(NoUsernameProvidedException error) {
        return wrapException(HttpStatus.BAD_REQUEST, error);
    }


    private ResponseEntity<ErrorResponse> wrapException(HttpStatus status, Exception exception) {
        ErrorResponse response = new ErrorResponse(status.value(), exception.getMessage());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
