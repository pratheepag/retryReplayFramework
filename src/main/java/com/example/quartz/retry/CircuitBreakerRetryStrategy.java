package com.example.quartz.retry;

import org.springframework.beans.factory.annotation.Value;

public class CircuitBreakerRetryStrategy implements RetryStrategy {

  @Value("${retry.circuit-breaker.failure-threshold}")
  private int failureThreshold;

  @Value("${retry.circuit-breaker.reset-timeout}")
  private long resetTimeout;

  private int failureCount;
  private long lastFailureTime;

  @Override
  public long getNextInterval(int retryCount) {
    if (failureCount >= failureThreshold) {
      if (System.currentTimeMillis() - lastFailureTime > resetTimeout) {
        failureCount = 0;
      } else {
        throw new RuntimeException("Circuit breaker is open");
      }
    } 
    failureCount++;
    lastFailureTime = System.currentTimeMillis();
    return 0;
  }
}