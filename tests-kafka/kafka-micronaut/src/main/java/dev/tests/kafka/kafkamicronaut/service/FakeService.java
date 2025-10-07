package dev.tests.kafka.kafkamicronaut.service;


import java.io.IOException;

import dev.tests.kafka.kafkamicronaut.message.TestAMessage;
import dev.tests.kafka.kafkamicronaut.message.TestBMessage;
import dev.tests.kafka.kafkaspring.avro.TestCMessage;
import io.micronaut.serde.ObjectMapper;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Singleton
@RequiredArgsConstructor
@Slf4j
public class FakeService {

    private final ObjectMapper jacksonMapper;


    public void merge(String key, TestAMessage testMessage) throws IOException {
        String messageJson = jacksonMapper.writeValueAsString(testMessage);
        log.info("receive A : {} -- {}", key, messageJson);
    }

    public void merge(String key, TestBMessage testMessage) throws IOException {
        String messageJson = jacksonMapper.writeValueAsString(testMessage);
        log.info("receive B : {} -- {}", key, messageJson);
    }

    public void merge(String key, TestCMessage testMessage) {
        log.info("receive C : {} -- {}", key, testMessage);
    }

}
