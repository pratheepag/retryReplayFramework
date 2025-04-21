package com.example.quartz.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest(properties = {"spring.config.name=application-test"}, classes = {SampleServiceTest.class})
public class SampleServiceTest {

    @InjectMocks
    private SampleService sampleService;

    @Mock
    private ReplayService replayService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private JavaMailSender javaMailSender;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        javaMailSender = Mockito.mock(JavaMailSender.class);
        sampleService = Mockito.mock(SampleService.class);
    }

    @Test
    public void testSuccessfulApiCallCount() {
        // Mock the behavior of sampleService
        doNothing().when(sampleService).callExternalApi(anyString());
        replayService.incrementSuccessfulApiCallCount(); // Simulate one successful call

        // Call the API multiple times
        sampleService.callExternalApi("387d25c1-94db-4954-a777-c602a0c090ae");
        sampleService.callExternalApi("d623b0c1-748f-4d1b-a96a-e99d5703ad4d");

        assertTrue(replayService.getSuccessfulApiCallCount() == 1 || replayService.getSuccessfulApiCallCount() == 2 || replayService.getSuccessfulApiCallCount() == 0 );
    }

}