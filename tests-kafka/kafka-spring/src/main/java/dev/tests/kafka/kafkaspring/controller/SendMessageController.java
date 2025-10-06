package dev.tests.kafka.kafkaspring.controller;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.tests.kafka.kafkaspring.avro.TestCMessage;
import dev.tests.kafka.kafkaspring.config.KafkaConstants;
import dev.tests.kafka.kafkaspring.message.TestAMessage;
import dev.tests.kafka.kafkaspring.message.TestBMessage;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SendMessageController {

    private int keyCounter = 0;

    private final KafkaTemplate<String, TestAMessage> kafkaTemplateA;
    private final KafkaTemplate<String, TestBMessage> kafkaTemplateB;
    private final KafkaTemplate<String, TestCMessage> kafkaTemplateC;


    @GetMapping("sendA")
    public String sendMessageA() {
        keyCounter++;
        TestAMessage testMessage = new TestAMessage(keyCounter, LocalDateTime.now().toString());
        String key = "k-spring-" + keyCounter;
        kafkaTemplateA.send(KafkaConstants.TOPIC_A, key, testMessage);
        return key;
    }

    @GetMapping("sendB")
    public String sendMessageB() {
        keyCounter++;
        LocalDateTime now = LocalDateTime.now();
        TestBMessage testMessage = new TestBMessage(keyCounter, now.toString(), now.toEpochSecond(ZoneOffset.UTC));
        String key = "k-spring-" + keyCounter;
        kafkaTemplateB.send(KafkaConstants.TOPIC_B, key, testMessage);
        return key;
    }

    @GetMapping("sendC")
    public String sendMessageC() {
        keyCounter++;
        LocalDateTime now = LocalDateTime.now();
        List<CharSequence> extra = List.of("abc", "def");
        TestCMessage testMessage = new TestCMessage(keyCounter, now.toString(), now.toEpochSecond(ZoneOffset.UTC), extra, "data");
        String key = "k-spring-" + keyCounter;
        kafkaTemplateC.send(KafkaConstants.TOPIC_C, key, testMessage);
        return key;
    }

}
