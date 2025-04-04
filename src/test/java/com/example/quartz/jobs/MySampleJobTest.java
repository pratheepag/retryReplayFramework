package com.example.quartz.jobs;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.example.quartz.factory.RetryFactory;
import com.example.quartz.service.SampleService;

public class MySampleJobTest {

    @Mock
    private SampleService sampleService;

    @Mock
    private RetryFactory retryFactory;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private Random jitterRandom;

    @InjectMocks
    private MySampleJob mySampleJob;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mySampleJob.setRetryFactory(retryFactory); // Use the setter to inject the mock RetryFactory
    }

    @Test
    public void testExecuteWithRetries() throws Exception {
        when(jitterRandom.nextInt(anyInt())).thenReturn(500); // Mock Random behavior
        doNothing().when(retryFactory).executeWithRetry(anyInt());

        mySampleJob.execute(jobExecutionContext);

        verify(retryFactory, atLeastOnce()).executeWithRetry(anyInt());
    }
}