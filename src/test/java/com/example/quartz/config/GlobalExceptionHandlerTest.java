package com.example.quartz.config;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    public void testHandleRuntimeException() {
        RuntimeException ex = new RuntimeException("Test runtime exception");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<String> response = exceptionHandler.handleRuntimeException(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred: Test runtime exception", response.getBody());
    }

    @Test
    public void testHandleGenericException() {
        Exception ex = new Exception("Test generic exception");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<String> response = exceptionHandler.handleGenericException(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred: Test generic exception", response.getBody());
    }
}