package dev.tests.kafka.kafkamicronaut.listener;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import dev.tests.kafka.kafkamicronaut.message.TestAMessage;
import dev.tests.kafka.kafkamicronaut.message.TestBMessage;
import dev.tests.kafka.kafkamicronaut.service.FakeService;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
class MessageKafkaJsonListenerTest {

    @Inject
    private MessageKafkaJsonListener messageKafkaJsonListener;

    @Inject
    private FakeService fakeService;


    @MockBean(FakeService.class)
    FakeService mockFakeService() {
        return Mockito.mock(FakeService.class);
    }

    @Test
    void listenTopicA() throws IOException {
        String key = "k-test-1";
        TestAMessage testMessage = new TestAMessage();
        messageKafkaJsonListener.listenTopicA(new ConsumerRecord<>("topic", 0, 1L, key, testMessage));

        Mockito.verify(fakeService).merge(key, testMessage);
    }

    @Test
    void listenTopicB() throws IOException {
        String key = "k-test-1";
        TestBMessage testMessage = new TestBMessage();
        messageKafkaJsonListener.listenTopicB(new ConsumerRecord<>("topic", 0, 1L, key, testMessage));

        Mockito.verify(fakeService).merge(key, testMessage);
    }

}
