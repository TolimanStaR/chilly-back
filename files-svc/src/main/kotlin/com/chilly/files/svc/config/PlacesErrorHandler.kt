package com.chilly.files.svc.config

import org.chilly.common.exception.GlobalErrorHandler
import org.springframework.web.bind.annotation.ControllerAdvice

@ControllerAdvice
class PlacesErrorHandler : GlobalErrorHandler()