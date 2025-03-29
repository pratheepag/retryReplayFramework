package com.example.quartz.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class QuartzHealthIndicator implements HealthIndicator {

	@Autowired
	private Scheduler scheduler;

	@Override
	public Health health() {
		try {
			if (scheduler.isStarted() && !scheduler.isInStandbyMode()) {
				return Health.up().withDetail("Quartz", "Running").build();
			} else {
				return Health.down().withDetail("Quartz", "Not Running").build();
			}
		} catch (SchedulerException e) {
			return Health.down().withDetail("Quartz", "Error").withException(e).build();
		}
	}
}