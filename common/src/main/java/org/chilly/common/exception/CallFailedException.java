package org.chilly.common.exception;

/**
 * used when a certain call to webClient or restTemplate fails
 */
public class CallFailedException extends BaseAppException {
    public CallFailedException(String s) {
        super(s);
    }
}
