package dev.tests.kafka.kafkamicronaut.kafka;

import dev.tests.kafka.kafkamicronaut.config.KafkaConstants;
import dev.tests.kafka.kafkaspring.avro.TestCMessage;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient("avro-client")
public interface MyKafkaAvroClient {

    @Topic(KafkaConstants.TOPIC_C)
    void sendMessageC(@KafkaKey String key, TestCMessage message);

}
