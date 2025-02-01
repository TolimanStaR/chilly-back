package org.chilly.common.exception;

/**
 * Thrown when Entity cannot be found
 */
public class NoSuchEntityException extends BaseAppException {
    public NoSuchEntityException(String s) {
        super(s);
    }
}
