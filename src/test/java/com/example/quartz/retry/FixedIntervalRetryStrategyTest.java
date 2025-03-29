package com.example.quartz.retry;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class FixedIntervalRetryStrategyTest {

    @Test
    public void testFixedIntervalRetryStrategy() {
        FixedIntervalRetryStrategy strategy = new FixedIntervalRetryStrategy();
        ReflectionTestUtils.setField(strategy, "fixedInterval", 1000);

        // Test logic here
        assertEquals(1000L, strategy.getNextInterval(1));
    }

    @Test
    public void testGetNextInterval() {
        FixedIntervalRetryStrategy strategy = new FixedIntervalRetryStrategy();
        ReflectionTestUtils.setField(strategy, "fixedInterval", 200);

        long interval = strategy.getNextInterval(3);
        assertEquals(200L, interval, "Interval should always be fixed");
    }
}