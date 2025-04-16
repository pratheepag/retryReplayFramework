package com.example.quartz.jobs;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.example.quartz.service.RetryService;
import com.example.quartz.service.SampleService;

public class MySampleJobTest {

    @Mock
    private SampleService sampleService;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private Random jitterRandom;

    @Mock
    private RetryService retryService;

    @InjectMocks
    private MySampleJob mySampleJob;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mySampleJob.setRetryService(retryService); // Inject the mock RetryService
    }

    @Test
    public void testExecuteWithRetries() throws Exception {
        doNothing().when(retryService).executeFixedIntervalRetry(anyInt());
        doNothing().when(retryService).executeExponentialBackoffRetry(anyInt());
        doNothing().when(retryService).executeJitterRetry(anyInt());
        doNothing().when(retryService).executeCircuitBreakerRetry(anyInt());

        // Simulate retryCount reaching the range for Exponential Backoff Retry
        mySampleJob.execute(jobExecutionContext);

        verify(retryService, atLeastOnce()).executeFixedIntervalRetry(anyInt());
       // verify(retryService, atLeastOnce()).executeExponentialBackoffRetry(anyInt());
        //verify(retryService, atLeastOnce()).executeJitterRetry(anyInt());
        //verify(retryService, atLeastOnce()).executeCircuitBreakerRetry(anyInt());
    }
}