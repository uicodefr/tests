package dev.tests.kafka.kafkaspring.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.tests.kafka.kafkaspring.avro.TestCMessage;
import dev.tests.kafka.kafkaspring.config.KafkaConstants;
import dev.tests.kafka.kafkaspring.message.TestBMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageKafkaListener {

    private final ObjectMapper jacksonMapper;


    @KafkaListener(topics = KafkaConstants.TOPIC_B)
    public void listenTopicB(ConsumerRecord<String, TestBMessage> consumerRecord) throws JsonProcessingException {
        TestBMessage testMessage = consumerRecord.value();
        String key = consumerRecord.key();
        String messageJson = jacksonMapper.writeValueAsString(testMessage);
        log.info("receive B : {} -- {}", key, messageJson);
    }

    @KafkaListener(topics = KafkaConstants.TOPIC_C, containerFactory = "kafkaListenerContainerAvroFactory")
    public void listenTopicC(ConsumerRecord<String, TestCMessage> consumerRecord) {
        TestCMessage testMessage = consumerRecord.value();
        String key = consumerRecord.key();
        log.info("receive C : {} -- {}", key, testMessage);
    }

}
