package com.example.quartz.retry;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CircuitBreakerRetryStrategyTest {

    @Test
    public void testCircuitBreakerRetryStrategy() {
    	CircuitBreakerRetryStrategy strategy = new CircuitBreakerRetryStrategy();
        ReflectionTestUtils.setField(strategy, "failureCount", 3);
        ReflectionTestUtils.setField(strategy, "resetTimeout", 5000L);
        ReflectionTestUtils.setField(strategy, "lastFailureTime", System.currentTimeMillis());
        
        assertThrows(RuntimeException.class, () -> {
            long interval = strategy.getNextInterval(9999);
        });
    }
}