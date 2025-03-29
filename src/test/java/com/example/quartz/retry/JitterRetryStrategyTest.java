package com.example.quartz.retry;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class JitterRetryStrategyTest {

    @Test
    public void testJitterRetryStrategy() {
        JitterRetryStrategy strategy = new JitterRetryStrategy();
        ReflectionTestUtils.setField(strategy, "baseInterval", 1000L);

        // Test logic here
        long interval = strategy.getNextInterval(1);
        assertTrue(interval >= 1000L && interval < 2000L);
    }
}