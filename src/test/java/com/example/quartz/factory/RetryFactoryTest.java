package com.example.quartz.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Random;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;

import com.example.quartz.service.SampleService;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;

class RetryFactoryTest {

    @Mock
    private RetryTemplate fixedIntervalRetryTemplate;

    @Mock
    private RetryTemplate exponentialBackoffRetryTemplate;

    @Mock
    private CircuitBreaker circuitBreaker;

    @Mock
    private SampleService sampleService;

    @Mock
    private Random jitterRandom;

    @InjectMocks
    private RetryFactory retryFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        retryFactory.setDelayHandler(() -> {}); // Inject a mock delay handler to bypass Thread.sleep
    }

    @Test
    void testFixedIntervalRetry() throws Throwable {
        when(fixedIntervalRetryTemplate.execute(any())).thenAnswer(invocation -> {
            RetryCallback<?, ?> callback = invocation.getArgument(0);
            return callback.doWithRetry(null);
        });

        retryFactory.executeWithRetry(2);

        verify(fixedIntervalRetryTemplate, times(1)).execute(any());
    }

    @Test
    void testExponentialBackoffRetry() throws Throwable {
        when(exponentialBackoffRetryTemplate.execute(any())).thenAnswer(invocation -> {
            RetryCallback<?, ?> callback = invocation.getArgument(0);
            return callback.doWithRetry(null);
        });

        retryFactory.executeWithRetry(5);

        verify(exponentialBackoffRetryTemplate, times(1)).execute(any());
    }

    @Test
    void testJitterRetry() throws Exception {
        when(jitterRandom.nextInt(anyInt())).thenReturn(500);
        doAnswer(invocation -> null).when(sampleService).callExternalApi(); // Mock API call

        retryFactory.executeWithRetry(8);

        verify(sampleService, times(1)).callExternalApi();
    }

    @Test
    void testCircuitBreakerRetry() throws Exception {
        when(circuitBreaker.getState()).thenReturn(CircuitBreaker.State.CLOSED);
        when(circuitBreaker.executeSupplier(any())).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });

        retryFactory.executeWithRetry(10);

        verify(circuitBreaker, times(1)).executeSupplier(any());
    }

    @Test
    void testJitterRetryUpToNineAttempts() throws Exception {
        when(jitterRandom.nextInt(anyInt())).thenReturn(500);
        doAnswer(invocation -> null).when(sampleService).callExternalApi(); // Mock API call

        retryFactory.executeWithRetry(9);

        verify(sampleService, times(1)).callExternalApi();
    }

    @Test
    void testCircuitBreakerRetryBeyondNineAttempts() throws Exception {
        when(circuitBreaker.getState()).thenReturn(CircuitBreaker.State.CLOSED);
        when(circuitBreaker.executeSupplier(any())).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });

        retryFactory.executeWithRetry(10);

        verify(circuitBreaker, times(1)).executeSupplier(any());
    }

    @Test
    void testCircuitBreakerOpenState() {
        when(circuitBreaker.getState()).thenReturn(CircuitBreaker.State.OPEN);
        Exception exception = assertThrows(RuntimeException.class, () -> retryFactory.executeWithRetry(10));
        assertEquals("Circuit Breaker is open. Stopping retries.", exception.getMessage());
    }
}