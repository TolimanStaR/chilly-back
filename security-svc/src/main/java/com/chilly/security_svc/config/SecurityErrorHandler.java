package com.chilly.security_svc.config;

import org.chilly.common.exception.GlobalErrorHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class SecurityErrorHandler extends GlobalErrorHandler {
}
