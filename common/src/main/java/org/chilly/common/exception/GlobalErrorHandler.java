package org.chilly.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

/**
 * Gracefully handles all the exception types from this module.
 * To be used extend this class and annotate with {@link org.springframework.web.bind.annotation.ControllerAdvice}
 */
public class GlobalErrorHandler {

    private static final Map<Class<? extends BaseAppException>, HttpStatus> STATUS_CODE_MAPPINGS = Map.ofEntries(
            Map.entry(NoSuchEntityException.class, HttpStatus.NOT_FOUND),
            Map.entry(CallFailedException.class, HttpStatus.BAD_REQUEST),
            Map.entry(EmptyDataException.class, HttpStatus.NOT_ACCEPTABLE),
            Map.entry(AccessDeniedException.class, HttpStatus.FORBIDDEN)
    );

    @ExceptionHandler(BaseAppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(BaseAppException exception) {
        for (Map.Entry<Class<? extends BaseAppException>, HttpStatus> entry : STATUS_CODE_MAPPINGS.entrySet()) {
            if (entry.getKey().equals(exception.getClass())) {
                return wrapException(entry.getValue(), exception);
            }
        }
        return wrapException(HttpStatus.INTERNAL_SERVER_ERROR, new RuntimeException("Unexpected exception", exception));
    }

    private ResponseEntity<ErrorResponse> wrapException(HttpStatus status, Exception exception) {
        ErrorResponse response = new ErrorResponse(status.value(), exception.getMessage());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
