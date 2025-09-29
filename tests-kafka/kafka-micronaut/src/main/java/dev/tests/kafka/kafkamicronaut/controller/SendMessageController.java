package dev.tests.kafka.kafkamicronaut.controller;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import dev.tests.kafka.kafkamicronaut.kafka.MyKafkaClient;
import dev.tests.kafka.kafkamicronaut.message.TestAMessage;
import dev.tests.kafka.kafkamicronaut.message.TestBMessage;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller
public class SendMessageController {

    private int keyCounter = 0;

    private final MyKafkaClient myKafkaClient;

    public SendMessageController(MyKafkaClient myKafkaClient) {
        this.myKafkaClient = myKafkaClient;
    }

    @Get(value = "sendA", produces = MediaType.TEXT_PLAIN)
    public String sendMessageA() {
        keyCounter++;
        TestAMessage testMessage = new TestAMessage(keyCounter, LocalDateTime.now().toString());
        String key = "k-micronaut-" + keyCounter;
        myKafkaClient.sendMessageA(key, testMessage);
        return key;
    }

    @Get(value ="sendB", produces = MediaType.TEXT_PLAIN)
    public String sendMessageB() {
        keyCounter++;
        LocalDateTime now = LocalDateTime.now();
        TestBMessage testMessage = new TestBMessage(keyCounter, now.toString(), now.toEpochSecond(ZoneOffset.UTC));
        String key = "k-micronaut-" + keyCounter;
        myKafkaClient.sendMessageB(key, testMessage);
        return key;
    }

}
