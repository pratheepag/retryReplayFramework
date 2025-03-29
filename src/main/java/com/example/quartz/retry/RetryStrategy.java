package com.example.quartz.retry;

public interface RetryStrategy {
  long getNextInterval(int retryCount);
}