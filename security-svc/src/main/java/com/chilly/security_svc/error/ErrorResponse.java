package com.chilly.security_svc.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private int statusCode;
    private String message;
}
