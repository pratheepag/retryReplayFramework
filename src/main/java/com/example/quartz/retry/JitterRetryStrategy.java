package com.example.quartz.retry;

import java.util.Random;
import org.springframework.beans.factory.annotation.Value;

public class JitterRetryStrategy implements RetryStrategy {

  @Value("${retry.backoff.initial-interval}")
  private long baseInterval;

  private final Random random = new Random();

  @Override
  public long getNextInterval(int retryCount) {
    return baseInterval + random.nextInt((int) baseInterval);
  }
}