package dev.tests.kafka.kafkaspring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest
@EmbeddedKafka
class KafkaSpringApplicationTests {

    @Test
    void contextLoads() {
        // The application start in test without errors
    }

}
