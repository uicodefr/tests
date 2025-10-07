package dev.tests.kafka.kafkamicronaut.listener;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.tests.kafka.kafkamicronaut.message.TestAMessage;
import dev.tests.kafka.kafkamicronaut.message.TestBMessage;
import dev.tests.kafka.kafkamicronaut.service.FakeService;

@ExtendWith(MockitoExtension.class)
class MessageKafkaListenerJsonJUnitTest {

    @Mock
    private FakeService fakeService;

    @InjectMocks
    private MessageKafkaJsonListener messageKafkaListener;

    @Test
    void listenTopicA() throws IOException {
        String key = "k-test-1";
        TestAMessage testMessage = new TestAMessage();
        messageKafkaListener.listenTopicA(new ConsumerRecord<>("topic", 0, 1L, key, testMessage));

        Mockito.verify(fakeService).merge(key, testMessage);
    }

    @Test
    void listenTopicB() throws IOException {
        String key = "k-test-1";
        TestBMessage testMessage = new TestBMessage();
        messageKafkaListener.listenTopicB(new ConsumerRecord<>("topic", 0, 1L, key, testMessage));

        Mockito.verify(fakeService).merge(key, testMessage);
    }

}
