package com.example.quartz.jobs;

import com.example.quartz.retry.RetryStrategy;
import com.example.quartz.retry.RetryStrategyFactory;
import com.example.quartz.service.SampleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import static org.mockito.Mockito.*;

public class MySampleJobTest {

    @Mock
    private SampleService sampleService;

    @Mock
    private RetryStrategyFactory retryStrategyFactory;

    @Mock
    private RetryStrategy retryStrategy;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @InjectMocks
    private MySampleJob mySampleJob;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExecuteWithRetries() throws Exception {
        when(retryStrategyFactory.getRetryStrategy(anyInt())).thenReturn(retryStrategy);
        when(retryStrategy.getNextInterval(anyInt())).thenReturn(100L);

        doThrow(new RuntimeException("Test Exception"))
                .doNothing()
                .when(sampleService).callExternalApi();

        mySampleJob.execute(jobExecutionContext);

        verify(sampleService, times(2)).callExternalApi();
        verify(retryStrategyFactory, atLeastOnce()).getRetryStrategy(anyInt());
    }
}