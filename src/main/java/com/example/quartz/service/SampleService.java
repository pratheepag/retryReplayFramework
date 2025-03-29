package com.example.quartz.service;

import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SampleService {

  private final Random random = new Random();

  @Autowired
  private ReplayService replayService;

  public void callExternalApi() {
    // Simulate a 50% chance of failure
    if (random.nextBoolean()) {
      String errorId = "Error-" + System.currentTimeMillis();
      String payload = UUID.randomUUID().toString(); // Generate a random string as payload
      replayService.logFailedCall(errorId, payload); // Log the failed call with payload
      throw new RuntimeException("Simulated API failure with ID: " + errorId);
    }
    System.out.println("API call succeeded");
    replayService.incrementSuccessfulApiCallCount(); // Increment successful API call count
  }
}
