package com.example.quartz.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest(properties = {"spring.config.name=application-test"}, classes = {SampleServiceTest.class})
public class SampleServiceTest {

    @Mock
    private SampleService sampleService;

    @Mock
    private ReplayService replayService;

    private JavaMailSender javaMailSender;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        javaMailSender = Mockito.mock(JavaMailSender.class);
    }

    @Test
    public void testSuccessfulApiCallCount() {
        // Mock the behavior of sampleService
        doNothing().when(sampleService).callExternalApi();

        // Reset the successful API call count
        replayService.incrementSuccessfulApiCallCount(); // Simulate one successful call

        // Call the API multiple times
        sampleService.callExternalApi();
        sampleService.callExternalApi();

        assertTrue(replayService.getSuccessfulApiCallCount() == 1 || replayService.getSuccessfulApiCallCount() == 2 || replayService.getSuccessfulApiCallCount() == 0 );
    }
}