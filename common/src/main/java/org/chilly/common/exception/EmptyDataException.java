package org.chilly.common.exception;

/**
 * Thrown when required value is not provided
 */
public class EmptyDataException extends BaseAppException {
    public EmptyDataException(String s) {
        super(s);
    }
}
