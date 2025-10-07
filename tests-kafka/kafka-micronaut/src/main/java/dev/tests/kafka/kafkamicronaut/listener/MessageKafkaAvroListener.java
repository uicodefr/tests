package dev.tests.kafka.kafkamicronaut.listener;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import dev.tests.kafka.kafkamicronaut.config.KafkaConstants;
import dev.tests.kafka.kafkamicronaut.service.FakeService;
import dev.tests.kafka.kafkaspring.avro.TestCMessage;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.context.annotation.Property;
import lombok.RequiredArgsConstructor;

@KafkaListener(value = "avro-listener", properties = {
    // Need to define group_id here because we need a different value on the listener
    @Property(name = "group.id", value = KafkaConstants.GROUP_ID)
})
@RequiredArgsConstructor
public class MessageKafkaAvroListener {

    private final FakeService fakeService;

    @Topic(KafkaConstants.TOPIC_C)
    public void listenTopicC(ConsumerRecord<String, TestCMessage> consumerRecord) throws IOException {
        String key = consumerRecord.key();
        TestCMessage testMessage = consumerRecord.value();
        fakeService.merge(key, testMessage);
    }

}
