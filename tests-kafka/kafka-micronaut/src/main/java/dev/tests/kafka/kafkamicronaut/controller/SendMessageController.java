package dev.tests.kafka.kafkamicronaut.controller;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import dev.tests.kafka.kafkamicronaut.kafka.MyKafkaAvroClient;
import dev.tests.kafka.kafkamicronaut.kafka.MyKafkaJsonClient;
import dev.tests.kafka.kafkamicronaut.message.TestAMessage;
import dev.tests.kafka.kafkamicronaut.message.TestBMessage;
import dev.tests.kafka.kafkaspring.avro.TestCMessage;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SendMessageController {

    private int keyCounter = 0;

    private final MyKafkaJsonClient myKafkaJsonClient;
    
    private final MyKafkaAvroClient myKafkaAvroClient;


    @Get(value = "sendA", produces = MediaType.TEXT_PLAIN)
    public String sendMessageA() {
        keyCounter++;
        TestAMessage testMessage = new TestAMessage(keyCounter, LocalDateTime.now().toString());
        String key = "k-micronaut-" + keyCounter;
        myKafkaJsonClient.sendMessageA(key, testMessage);
        return key;
    }

    @Get(value ="sendB", produces = MediaType.TEXT_PLAIN)
    public String sendMessageB() {
        keyCounter++;
        LocalDateTime now = LocalDateTime.now();
        TestBMessage testMessage = new TestBMessage(keyCounter, now.toString(), now.toEpochSecond(ZoneOffset.UTC));
        String key = "k-micronaut-" + keyCounter;
        myKafkaJsonClient.sendMessageB(key, testMessage);
        return key;
    }

    @Get(value ="sendC", produces = MediaType.TEXT_PLAIN)
    public String sendMessageC() {
        keyCounter++;
        LocalDateTime now = LocalDateTime.now();
        List<CharSequence> extra = List.of("abc", "def");
        TestCMessage testMessage = new TestCMessage(keyCounter, now.toString(), now.toEpochSecond(ZoneOffset.UTC), extra, "data");
        String key = "k-micronaut-" + keyCounter;
        myKafkaAvroClient.sendMessageC(key, testMessage);
        return key;
    }

}
