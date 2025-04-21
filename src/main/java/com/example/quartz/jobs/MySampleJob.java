package com.example.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.quartz.factory.RetryFactory;

import java.util.UUID;

@Component
public class MySampleJob implements Job {

	@Autowired
	private RetryFactory retryFactory;

	private static final Logger logger = LoggerFactory.getLogger(MySampleJob.class);

	public void setRetryFactory(RetryFactory retryFactory) {
		this.retryFactory = retryFactory;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String payload = UUID.randomUUID().toString(); // Generate a random string as payload
		int retryCount = 1;
		try {
			while (true) {
				try {
					retryFactory.executeWithRetry(retryCount, payload);
					break; // Exit loop on success
				} catch (Exception e) {
					retryCount++;
					logger.error("Retry attempt " + retryCount + " failed for payload: " + payload, e);
				}
			}
		} catch (Exception e) {
			throw new JobExecutionException("Retry interrupted for payload: " + payload, e);
		}
	}
}
