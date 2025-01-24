package org.chilly.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Wrapper to represent Error response on request
 */
@AllArgsConstructor
@Getter
public class ErrorResponse {
    private int statusCode;
    private String message;
}
