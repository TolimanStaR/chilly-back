package com.chilly.feedback_svc.config

import org.chilly.common.exception.GlobalErrorHandler
import org.springframework.web.bind.annotation.ControllerAdvice

@ControllerAdvice
class PlacesErrorHandler : GlobalErrorHandler()