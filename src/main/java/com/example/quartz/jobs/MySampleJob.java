package com.example.quartz.jobs;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.quartz.retry.RetryStrategy;
import com.example.quartz.retry.RetryStrategyFactory;
import com.example.quartz.service.SampleService;

@Component
public class MySampleJob implements Job {

  @Autowired
  private SampleService sampleService;

  @Autowired
  private RetryStrategyFactory retryStrategyFactory;
  
  private static final Logger logger = LoggerFactory.getLogger(MySampleJob.class);

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    int retryCount = 0;
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    try {
      while (true) {
        try {
          sampleService.callExternalApi();
          break; // Exit loop on success
        } catch (Exception e) {
          retryCount++;
          RetryStrategy retryStrategy = retryStrategyFactory.getRetryStrategy(retryCount);
          long nextInterval = retryStrategy.getNextInterval(retryCount);
          logger.info("Retrying in " + nextInterval + "ms (Attempt " + retryCount
              + ") using strategy: " + retryStrategy.getClass().getSimpleName());

          scheduler.schedule(() -> {
          }, nextInterval, TimeUnit.MILLISECONDS).get();
        }
      }
    } catch (Exception e) {
      throw new JobExecutionException("Retry interrupted", e);
    } finally {
      scheduler.shutdown();
    }
  }
}
