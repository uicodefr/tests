package dev.tests.kafka.kafkamicronaut.kafka;

import dev.tests.kafka.kafkamicronaut.config.KafkaConstants;
import dev.tests.kafka.kafkamicronaut.message.TestAMessage;
import dev.tests.kafka.kafkamicronaut.message.TestBMessage;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface MyKafkaClient {

    @Topic(KafkaConstants.TOPIC_A)
    void sendMessageA(@KafkaKey String key, TestAMessage message);

    @Topic(KafkaConstants.TOPIC_B)
    void sendMessageB(@KafkaKey String key, TestBMessage message);

}
