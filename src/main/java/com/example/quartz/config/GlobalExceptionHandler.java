package com.example.quartz.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handleRuntimeException(RuntimeException ex, WebRequest request) {
    logger.error("RuntimeException occurred: {}", ex.getMessage(), ex);
    return new ResponseEntity<>("An error occurred: " + ex.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGenericException(Exception ex, WebRequest request) {
    logger.error("Exception occurred: {}", ex.getMessage(), ex);
    return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}