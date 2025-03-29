package com.example.quartz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ReplayService {

  private static final Logger logger = LoggerFactory.getLogger(ReplayService.class);

  private final Map<String, String> failedCalls = new HashMap<>();
  private final AtomicInteger retryCount = new AtomicInteger(0);
  private final AtomicInteger replayCount = new AtomicInteger(0);
  private final AtomicInteger successfulApiCallCount = new AtomicInteger(0); // Track successful API
                                                                             // calls

  @Autowired
  private EmailService emailService;

  @Value("${mail.sender}")
  private String senderEmail;

  @Value("${mail.recipient}")
  private String recipientEmail;

  public void logFailedCall(String errorId, String payload) {
    failedCalls.put(errorId, payload);
    retryCount.incrementAndGet();
  }

  public List<String> getErrors() {
    return failedCalls.keySet().stream().collect(Collectors.toList());
  }

  public String getPayload(String errorId) {
    return failedCalls.get(errorId);
  }

  public void replayError(String errorId) {
    logger.info("Starting replay for error ID: {}", errorId);
    String payload = failedCalls.get(errorId);
    if (payload != null) {
      logger.info("Replaying error: " + errorId + " with payload: " + payload);
      boolean apiCallSuccess = simulateApiCall(payload);
      if (apiCallSuccess) {
        failedCalls.remove(errorId);
        replayCount.incrementAndGet();
        successfulApiCallCount.incrementAndGet();
        sendEmailNotification("Replay Successful", "Replay successful for error ID: " + errorId);
        logger.info("Replay successful for error ID: {}", errorId);
      } else {
        sendEmailNotification("Replay Failed", "Replay failed for error ID: " + errorId);
        logger.error("Replay failed for error ID: {}", errorId);
        throw new RuntimeException("Error ID not found: " + errorId);
      }
    } else {
      logger.error("Error ID not found: {}", errorId);
      throw new RuntimeException("Error ID not found: " + errorId);
    }
  }

  public int getRetryCount() {
    return retryCount.get();
  }

  public int getReplayCount() {
    return replayCount.get();
  }

  public int getSuccessfulApiCallCount() {
    return successfulApiCallCount.get();
  }

  public void incrementSuccessfulApiCallCount() {
    successfulApiCallCount.incrementAndGet();
  }

  private boolean simulateApiCall(String payload) {
    logger.debug("Simulating API call with payload: {}", payload);
    boolean success = Math.random() > 0.5; // Example: 50% chance of success
    if (!success) {
      logger.warn("API call failed for payload: {}", payload);
    }
    return success;
  }

  private void sendEmailNotification(String subject, String body) {
	  try {
          emailService.sendEmail(recipientEmail, subject, body);
          logger.info("Email notification sent: {}", subject);
      } catch (Exception e) {
    	  logger.error("Failed to send email notification:", e);
      }
    
  }
}