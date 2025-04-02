package com.example.quartz;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.config.name=application-test"}, classes = {QuartzApplicationTests.class})
class QuartzApplicationTests {

    @Test
    void contextLoads() {
    }

}
