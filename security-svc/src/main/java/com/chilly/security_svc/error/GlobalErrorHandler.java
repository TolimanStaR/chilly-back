package com.chilly.security_svc.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(UserNotSavedError.class)
    public ResponseEntity<ErrorResponse> handle(UserNotSavedError error) {
        return wrapException(HttpStatus.SERVICE_UNAVAILABLE, error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(UserNotFoundException error) {
        return wrapException(HttpStatus.NOT_FOUND, error);
    }

    @ExceptionHandler(NoUserForRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handle(NoUserForRefreshTokenException error) {
        return wrapException(HttpStatus.UNAUTHORIZED, error);
    }

    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handle(ExpiredRefreshTokenException error) {
        return wrapException(HttpStatus.UNAUTHORIZED, error);
    }

    @ExceptionHandler(NoUsernameProvidedException.class)
    public ResponseEntity<ErrorResponse> handle(NoUsernameProvidedException error) {
        return wrapException(HttpStatus.BAD_REQUEST, error);
    }
    @ExceptionHandler(UserAlreadyExitsException.class)
    public ResponseEntity<ErrorResponse> handle(UserAlreadyExitsException error) {
        return wrapException(HttpStatus.CONFLICT, error);
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<ErrorResponse> handle(WrongPasswordException error) {
        return wrapException(HttpStatus.FORBIDDEN, error);
    }

    @ExceptionHandler(UnableToSendEmailException.class)
    public ResponseEntity<ErrorResponse> handle(UnableToSendEmailException error) {
        return wrapException(HttpStatus.BAD_REQUEST, error);
    }

    @ExceptionHandler(CannotRecoverPasswordException.class)
    public ResponseEntity<ErrorResponse> handle(CannotRecoverPasswordException error) {
        return wrapException(HttpStatus.BAD_REQUEST, error);
    }

    private ResponseEntity<ErrorResponse> wrapException(HttpStatus status, Exception exception) {
        ErrorResponse response = new ErrorResponse(status.value(), exception.getMessage());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
