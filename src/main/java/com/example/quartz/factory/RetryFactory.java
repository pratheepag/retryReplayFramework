package com.example.quartz.factory;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import com.example.quartz.service.SampleService;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;

@Component
public class RetryFactory {

    private static final Logger logger = LoggerFactory.getLogger(RetryFactory.class);

    @Autowired
    private RetryTemplate fixedIntervalRetryTemplate;

    @Autowired
    private RetryTemplate exponentialBackoffRetryTemplate;

    @Autowired
    private CircuitBreaker circuitBreaker;

    @Autowired
    private Random jitterRandom;

    @Autowired
    private SampleService sampleService;

    private Runnable delayHandler = () -> {
        try {
            Thread.sleep(1000); // Default delay handler
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    };

    public void setDelayHandler(Runnable delayHandler) {
        this.delayHandler = delayHandler;
    }

    public void executeWithRetry(int retryCount, String payload) throws Exception {
        if (retryCount <= 3) {
            // Use Fixed Interval Retry for attempts up to 3
            logger.info("Executing Fixed Interval Retry with payload: " + payload + " and retry count: " + retryCount);

            fixedIntervalRetryTemplate.execute(retryContext -> {
                sampleService.callExternalApi(payload);
                return null;
            });
        } else if (retryCount <= 6) {
            // Use Exponential Backoff Retry for attempts 4 to 6
            logger.info("Executing Exponential Backoff Retry with payload: " + payload + " and retry count: " + retryCount);

            exponentialBackoffRetryTemplate.execute(retryContext -> {
                sampleService.callExternalApi(payload);
                return null;
            });
        } else if (retryCount <= 9) {
            // Use Jitter Retry for attempts 7 to 9
            long delay = (long) (Math.pow(2, retryCount) * 1000 + jitterRandom.nextInt(1000));
            logger.info("Retrying in " + delay + "ms (Attempt " + retryCount + ") with Jitter");
            delayHandler.run(); // Use the delay handler
            sampleService.callExternalApi(payload);
        } else {
            // Use Circuit Breaker for attempts beyond 9
            if (circuitBreaker.getState() == CircuitBreaker.State.OPEN) {
                logger.error("Circuit Breaker is open. Closing connection and stopping retries.");
                throw new RuntimeException("Circuit Breaker is open. Stopping retries.");
            }
            logger.info("Executing Circuit Breaker Retry with payload: " + payload + " and retry count: " + retryCount);

            circuitBreaker.executeSupplier(() -> {
                sampleService.callExternalApi(payload);
                return null;
            });
        }
    }
}