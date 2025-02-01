package org.chilly.common.exception;

/**
 * Base class for all provided exception types
 */
public class BaseAppException extends RuntimeException {
    public BaseAppException(String message) {
        super(message);
    }
}
