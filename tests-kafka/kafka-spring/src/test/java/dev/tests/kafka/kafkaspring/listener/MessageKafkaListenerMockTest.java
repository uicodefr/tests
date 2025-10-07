package dev.tests.kafka.kafkaspring.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;

import dev.tests.kafka.kafkaspring.avro.TestCMessage;
import dev.tests.kafka.kafkaspring.message.TestBMessage;
import dev.tests.kafka.kafkaspring.service.FakeService;

@ExtendWith(MockitoExtension.class)
class MessageKafkaListenerMockTest {

    @Mock
    private FakeService fakeService;

    @InjectMocks
    private MessageKafkaListener messageKafkaListener;


    @Test
    void listenTopicB() throws JsonProcessingException {
        String key = "k-test-1";
        TestBMessage testMessage = new TestBMessage();
        messageKafkaListener.listenTopicB(new ConsumerRecord<>("topic", 0, 1L, key, testMessage));

        Mockito.verify(fakeService).merge(key, testMessage);
    }

    @Test
    void listenTopicC() {
        String key = "k-test-1";
        TestCMessage testMessage = new TestCMessage();
        messageKafkaListener.listenTopicC(new ConsumerRecord<>("topic", 0, 1L, key, testMessage));

        Mockito.verify(fakeService).merge(key, testMessage);
    }

}
