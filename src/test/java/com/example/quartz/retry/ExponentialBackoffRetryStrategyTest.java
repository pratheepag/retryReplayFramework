package com.example.quartz.retry;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class ExponentialBackoffRetryStrategyTest {

    @Test
    public void testExponentialBackoffRetryStrategy() {
        ExponentialBackoffRetryStrategy strategy = new ExponentialBackoffRetryStrategy();
        ReflectionTestUtils.setField(strategy, "initialInterval", 1000L);
        ReflectionTestUtils.setField(strategy, "multiplier", 2.0);

        // Test logic here
        assertEquals(2000L, strategy.getNextInterval(1));
    }

    @Test
    public void testGetNextInterval() {
        ExponentialBackoffRetryStrategy strategy = new ExponentialBackoffRetryStrategy();
        ReflectionTestUtils.setField(strategy, "initialInterval", 100L);
        ReflectionTestUtils.setField(strategy, "multiplier", 2.0);

        long interval = strategy.getNextInterval(2);
        assertEquals(400L, interval, "Interval should match the expected exponential backoff");
    }
}