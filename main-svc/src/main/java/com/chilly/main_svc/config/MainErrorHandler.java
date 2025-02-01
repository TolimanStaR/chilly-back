package com.chilly.main_svc.config;

import org.chilly.common.exception.GlobalErrorHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class MainErrorHandler extends GlobalErrorHandler {
}
