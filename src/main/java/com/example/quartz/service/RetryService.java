package com.example.quartz.service;

import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetryService {

    private static final Logger logger = LoggerFactory.getLogger(RetryService.class);

    @Autowired
    private ProducerTemplate producerTemplate;

    public void executeFixedIntervalRetry(int attempt) {
        logger.info("Attempt {}: Executing Fixed Interval Retry", attempt);
        producerTemplate.sendBody("direct:fixedIntervalRetry", "Payload for Fixed Interval Retry");
    }

    public void executeExponentialBackoffRetry(int attempt) {
        logger.info("Attempt {}: Executing Exponential Backoff Retry", attempt);
        producerTemplate.sendBody("direct:exponentialBackoffRetry", "Payload for Exponential Backoff Retry");
    }

    public void executeJitterRetry(int attempt) {
        logger.info("Attempt {}: Executing Jitter Retry", attempt);
        producerTemplate.sendBody("direct:jitterRetry", "Payload for Jitter Retry");
    }

    public void executeCircuitBreakerRetry(int attempt) {
        logger.info("Attempt {}: Executing Circuit Breaker Retry", attempt);
        producerTemplate.sendBody("direct:circuitBreakerRetry", "Payload for Circuit Breaker Retry");
    }
}
