package dev.tests.kafka.kafkamicronaut.listener;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import dev.tests.kafka.kafkamicronaut.config.KafkaConstants;
import dev.tests.kafka.kafkamicronaut.message.TestAMessage;
import dev.tests.kafka.kafkamicronaut.message.TestBMessage;
import dev.tests.kafka.kafkamicronaut.service.FakeService;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.RequiredArgsConstructor;

@KafkaListener(groupId = KafkaConstants.GROUP_ID)
@RequiredArgsConstructor
public class MessageKafkaJsonListener {

    private final FakeService fakeService;

    @Topic(KafkaConstants.TOPIC_A)
    public void listenTopicA(ConsumerRecord<String, TestAMessage> consumerRecord) throws IOException {
        String key = consumerRecord.key();
        TestAMessage testMessage = consumerRecord.value();
        fakeService.merge(key, testMessage);
    }

    @Topic(KafkaConstants.TOPIC_B)
    public void listenTopicB(ConsumerRecord<String, TestBMessage> consumerRecord) throws IOException {
        String key = consumerRecord.key();
        TestBMessage testMessage = consumerRecord.value();
        fakeService.merge(key, testMessage);
    }

}
