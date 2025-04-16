package com.example.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.quartz.service.RetryService;

@Component
public class MySampleJob implements Job {

  @Autowired
  private RetryService retryService;

  private static final Logger logger = LoggerFactory.getLogger(MySampleJob.class);

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    int retryCount = 1;
    try {
      while (true) {
        try {
          if (retryCount <= 3) {
            retryService.executeFixedIntervalRetry(retryCount);
          } else if (retryCount <= 6) {
            retryService.executeExponentialBackoffRetry(retryCount);
          } else if (retryCount <= 9) {
            retryService.executeJitterRetry(retryCount);
          } else {
            retryService.executeCircuitBreakerRetry(retryCount);
          }
          break; // Exit loop on success
        } catch (Exception e) {
          retryCount++;
          logger.error("Retry attempt " + retryCount + " failed", e);
        }
      }
    } catch (Exception e) {
      throw new JobExecutionException("Retry interrupted", e);
    }
  }

  public void setRetryService(RetryService retryService) {
    this.retryService = retryService;
  }

}
