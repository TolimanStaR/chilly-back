package org.chilly.common.exception;

/**
 * Used when operation cannot be performed by current user
 */
public class AccessDeniedException extends BaseAppException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
