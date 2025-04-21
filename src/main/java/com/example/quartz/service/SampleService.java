package com.example.quartz.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SampleService {

	private final Random random = new Random();

	@Autowired
	private ReplayService replayService;

	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	public void callExternalApi(String payload) {
		try {
			// Simulate a 50% chance of failure
			if (random.nextBoolean()) {
				String errorId = "Error-" + System.currentTimeMillis();
				replayService.logFailedCall(errorId, payload);
				kafkaTemplate.send("fail-api-call", "payload : " + payload + " | errorId : " + errorId);
				throw new RuntimeException("Simulated API failure with ID: " + errorId);
			}
			System.out.println("API call succeeded");
			replayService.incrementSuccessfulApiCallCount();
			kafkaTemplate.send("success-api-call", "payload : " + payload);
		} catch (Exception e) {
			throw new RuntimeException("Simulated API failure with ID: " + e.getMessage());
		}

	}
}
