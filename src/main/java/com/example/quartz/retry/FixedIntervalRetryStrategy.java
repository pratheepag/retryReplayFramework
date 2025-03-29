package com.example.quartz.retry;

import org.springframework.beans.factory.annotation.Value;

public class FixedIntervalRetryStrategy implements RetryStrategy {

	@Value("${retry.backoff.initial-interval}")
	private int fixedInterval;

	@Override
	public long getNextInterval(int retryCount) {
		return fixedInterval;
	}
}