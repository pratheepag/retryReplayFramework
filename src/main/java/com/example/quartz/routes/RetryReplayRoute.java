package com.example.quartz.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.Resilience4jConfigurationDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RetryReplayRoute extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(RetryReplayRoute.class);
    
	public RetryReplayRoute(CamelContext camelContext) {
		super(camelContext);
	}

    @Override
    public void configure() throws Exception {
        logger.info("RetryReplayRoute: configure method is being executed...");

        // Circuit Breaker Configuration
        Resilience4jConfigurationDefinition resilience4jConfig = new Resilience4jConfigurationDefinition();
        resilience4jConfig
            .failureRateThreshold(50)
            .slidingWindowSize(10)
            .permittedNumberOfCallsInHalfOpenState(5)
            .waitDurationInOpenState(30);

        // Route with Fixed Interval Retry
        from("direct://fixedIntervalRetry")
            .onException(Exception.class)
                .maximumRedeliveries(3)
                .redeliveryDelay(5000) // Retry every 5 seconds
                .logRetryAttempted(true)
                .end()
            .to("bean:sampleService?method=callExternalApi");

        logger.info("Route 'direct:fixedIntervalRetry' is being initialized...");

        // Route with Exponential Backoff Retry
        from("direct:exponentialBackoffRetry")
            .onException(Exception.class)
                .maximumRedeliveries(5)
                .redeliveryDelay(1000) // Initial delay
                .backOffMultiplier(2.0) // Exponential backoff
                .logRetryAttempted(true)
                .end()
            .to("bean:sampleService?method=callExternalApi");

        // Route with Jitter Retry
        from("direct:jitterRetry")
            .onException(Exception.class)
                .maximumRedeliveries(5)
                .redeliveryDelay(1000) // Base delay
                .useExponentialBackOff()
                .backOffMultiplier(2.0)
                .onRedelivery(exchange -> {
                    long jitter = (long) (Math.random() * 1000); // Add random jitter
                    Thread.sleep(jitter);
                })
                .logRetryAttempted(true)
                .end()
            .to("bean:sampleService?method=callExternalApi");

        // Route with Circuit Breaker
        from("direct:circuitBreakerRetry")
            .circuitBreaker()
                .resilience4jConfiguration(resilience4jConfig)
                .to("bean:sampleService?method=callExternalApi")
            .end();
    }
}
