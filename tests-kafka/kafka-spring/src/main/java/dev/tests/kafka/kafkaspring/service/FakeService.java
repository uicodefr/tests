package dev.tests.kafka.kafkaspring.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.tests.kafka.kafkaspring.avro.TestCMessage;
import dev.tests.kafka.kafkaspring.message.TestBMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FakeService {

    private final ObjectMapper jacksonMapper;


    public void merge(String key, TestBMessage testMessage) throws JsonProcessingException {
        String messageJson = jacksonMapper.writeValueAsString(testMessage);
        log.info("receive B : {} -- {}", key, messageJson);
    }

    public void merge(String key, TestCMessage testMessage) {
        log.info("receive C : {} -- {}", key, testMessage);
    }

}
