package dev.tests.kafka.kafkamicronaut.listener;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.tests.kafka.kafkamicronaut.config.KafkaConstants;
import dev.tests.kafka.kafkamicronaut.message.TestAMessage;
import dev.tests.kafka.kafkamicronaut.message.TestBMessage;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.serde.ObjectMapper;

@KafkaListener(groupId = KafkaConstants.GROUP_ID)
public class MessageKafkaListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageKafkaListener.class);

    private final ObjectMapper jacksonMapper;

    public MessageKafkaListener(ObjectMapper jacksonMapper) {
        this.jacksonMapper = jacksonMapper;
    }

    @Topic(KafkaConstants.TOPIC_A)
    public void receiveTopicA(ConsumerRecord<String, TestAMessage> consumerRecord) throws IOException {
        TestAMessage testMessage = consumerRecord.value();
        String key = consumerRecord.key();
        String messageJson = jacksonMapper.writeValueAsString(testMessage);
        logger.info("receive A : {} -- {}", key, messageJson);
    }

    @Topic(KafkaConstants.TOPIC_B)
    public void receiveTopicB(ConsumerRecord<String, TestBMessage> consumerRecord) throws IOException {
        TestBMessage testMessage = consumerRecord.value();
        String key = consumerRecord.key();
        String messageJson = jacksonMapper.writeValueAsString(testMessage);
        logger.info("receive B : {} -- {}", key, messageJson);
    }

}
