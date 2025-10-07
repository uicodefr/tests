package dev.tests.kafka.kafkaspring.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import dev.tests.kafka.kafkaspring.avro.TestCMessage;
import dev.tests.kafka.kafkaspring.config.KafkaConstants;
import dev.tests.kafka.kafkaspring.message.TestBMessage;
import dev.tests.kafka.kafkaspring.service.FakeService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageKafkaListener {

    private final FakeService fakeService;


    @KafkaListener(topics = KafkaConstants.TOPIC_B)
    public void listenTopicB(ConsumerRecord<String, TestBMessage> consumerRecord) throws JsonProcessingException  {
        String key = consumerRecord.key();
        TestBMessage testMessage = consumerRecord.value();
        fakeService.merge(key, testMessage);
    }

    @KafkaListener(topics = KafkaConstants.TOPIC_C, containerFactory = "kafkaListenerContainerAvroFactory")
    public void listenTopicC(ConsumerRecord<String, TestCMessage> consumerRecord) {
        String key = consumerRecord.key();
        TestCMessage testMessage = consumerRecord.value();
        fakeService.merge(key, testMessage);
    }

}
