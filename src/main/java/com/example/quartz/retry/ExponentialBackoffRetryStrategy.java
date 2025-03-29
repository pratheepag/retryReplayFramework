package com.example.quartz.retry;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class ExponentialBackoffRetryStrategy implements RetryStrategy {

  private static final Logger logger = LoggerFactory
      .getLogger(ExponentialBackoffRetryStrategy.class);

  @Value("${retry.backoff.initial-interval}")
  private long initialInterval;

  @Value("${retry.backoff.multiplier}")
  private double multiplier;

  @Value("${retry.max-attempts}")
  private int maxAttempts;

  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  @Override
  public long getNextInterval(int retryCount) {
    return (long) (initialInterval * Math.pow(multiplier, retryCount));
  }

  public void executeWithRetry(Runnable task) {
    long currentInterval = initialInterval;
    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
      try {
        logger.info("Attempt {} with interval {} ms", attempt, currentInterval);
        task.run();
        logger.info("Task succeeded on attempt {}", attempt);
        return; // Exit if task succeeds
      } catch (Exception e) {
        logger.error("Task failed on attempt {}: {}", attempt, e.getMessage());
        if (attempt == maxAttempts) {
          logger.error("Max retry attempts reached. Giving up.");
          throw new RuntimeException("Max retry attempts reached", e);
        }
        try {
          scheduler.schedule(() -> {
          }, currentInterval, TimeUnit.MILLISECONDS).get();
        } catch (InterruptedException | ExecutionException ie) {
          logger.error("Retry interrupted: {}", ie.getMessage());
          Thread.currentThread().interrupt();
          throw new RuntimeException("Retry interrupted", ie);
        }
        currentInterval *= multiplier; // Increase interval for next attempt
      }
    }
  }
}