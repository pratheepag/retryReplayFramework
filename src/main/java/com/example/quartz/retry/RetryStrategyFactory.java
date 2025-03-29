package com.example.quartz.retry;

import org.springframework.stereotype.Component;

@Component
public class RetryStrategyFactory {

  public RetryStrategy getRetryStrategy(int retryCount) {
    if (retryCount <= 3) {
      return new FixedIntervalRetryStrategy();
    } else if (retryCount <= 6) {
      return new ExponentialBackoffRetryStrategy();
    } else if (retryCount <= 9) {
      return new JitterRetryStrategy();
    } else {
      return new CircuitBreakerRetryStrategy();
    }
  }
}