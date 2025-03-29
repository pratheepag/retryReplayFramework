package com.example.quartz.retry;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class RetryStrategyFactoryTest {

    private final RetryStrategyFactory factory = new RetryStrategyFactory();

    @Test
    public void testGetRetryStrategy() {
        RetryStrategy strategy1 = factory.getRetryStrategy(1);
        assertTrue(strategy1 instanceof FixedIntervalRetryStrategy);

        RetryStrategy strategy4 = factory.getRetryStrategy(4);
        assertTrue(strategy4 instanceof ExponentialBackoffRetryStrategy);

        RetryStrategy strategy7 = factory.getRetryStrategy(7);
        assertTrue(strategy7 instanceof JitterRetryStrategy);

        RetryStrategy strategy10 = factory.getRetryStrategy(10);
        assertTrue(strategy10 instanceof CircuitBreakerRetryStrategy);
    }

    @Test
    public void testGetRetryStrategyNotNull() {
        RetryStrategy strategy = factory.getRetryStrategy(1);
        assertNotNull(strategy, "RetryStrategy should not be null");
        assertTrue(strategy instanceof FixedIntervalRetryStrategy, "RetryStrategy should be of type FixedIntervalRetryStrategy");
    }
}