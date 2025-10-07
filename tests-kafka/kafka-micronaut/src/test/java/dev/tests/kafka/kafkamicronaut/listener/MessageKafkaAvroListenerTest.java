package dev.tests.kafka.kafkamicronaut.listener;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import dev.tests.kafka.kafkamicronaut.service.FakeService;
import dev.tests.kafka.kafkaspring.avro.TestCMessage;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
class MessageKafkaAvroListenerTest {

    @Inject
    private MessageKafkaAvroListener messageKafkaAvroListener;

    @Inject
    private FakeService fakeService;


    @MockBean(FakeService.class)
    FakeService mockFakeService() {
        return Mockito.mock(FakeService.class);
    }

    @Test
    void listenTopicC() throws IOException {
        String key = "k-test-1";
        TestCMessage testMessage = new TestCMessage();
        messageKafkaAvroListener.listenTopicC(new ConsumerRecord<>("topic", 0, 1L, key, testMessage));

        Mockito.verify(fakeService).merge(key, testMessage);
    }

}
