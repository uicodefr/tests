package dev.tests.kafka.kafkamicronaut.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import dev.tests.kafka.kafkamicronaut.kafka.MyKafkaAvroClient;
import dev.tests.kafka.kafkamicronaut.kafka.MyKafkaJsonClient;
import dev.tests.kafka.kafkamicronaut.message.TestAMessage;
import dev.tests.kafka.kafkamicronaut.message.TestBMessage;
import dev.tests.kafka.kafkaspring.avro.TestCMessage;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
class SendMessageControllerTest {

    @Inject
    @Client("/")
    private HttpClient client;

    @Inject
    private MyKafkaJsonClient myKafkaJsonClient;

    @Inject
    private MyKafkaAvroClient myKafkaAvroClient;


    @MockBean(MyKafkaJsonClient.class)
    MyKafkaJsonClient mockKafkaJsonClient() {
        return Mockito.mock(MyKafkaJsonClient.class);
    }

    @MockBean(MyKafkaAvroClient.class)
    MyKafkaAvroClient mockKafkaAvroClient() {
        return Mockito.mock(MyKafkaAvroClient.class);
    }

    @Test
    void sendMessageA() {
        HttpResponse<String> response = client.toBlocking().exchange("/sendA", String.class);
        Assertions.assertThat(response.code()).isEqualTo(200);
        Assertions.assertThat(response.body()).startsWith("k-micronaut-");

        ArgumentCaptor<TestAMessage> messageCaptor = ArgumentCaptor.forClass(TestAMessage.class);
        Mockito.verify(myKafkaJsonClient).sendMessageA(Mockito.startsWith("k-micronaut-"), messageCaptor.capture());

        TestAMessage testAMessage = messageCaptor.getValue();
        Assertions.assertThat(testAMessage).isNotNull();
        Assertions.assertThat(testAMessage.getId()).isPositive();
        Assertions.assertThat(testAMessage.getText()).isNotBlank();
        Assertions.assertThat(testAMessage.getData()).isEqualTo("data");
    }

    @Test
    void sendMessageB() {
        HttpResponse<String> response = client.toBlocking().exchange("/sendB", String.class);
        Assertions.assertThat(response.code()).isEqualTo(200);
        Assertions.assertThat(response.body()).startsWith("k-micronaut-");

        ArgumentCaptor<TestBMessage> messageCaptor = ArgumentCaptor.forClass(TestBMessage.class);
        Mockito.verify(myKafkaJsonClient).sendMessageB(Mockito.startsWith("k-micronaut-"), messageCaptor.capture());

        TestBMessage testBMessage = messageCaptor.getValue();
        Assertions.assertThat(testBMessage).isNotNull();
        Assertions.assertThat(testBMessage.getId()).isPositive();
        Assertions.assertThat(testBMessage.getText()).isNotBlank();
        Assertions.assertThat(testBMessage.getTimestamp()).isPositive();
        Assertions.assertThat(testBMessage.getData()).isEqualTo("data");
    }

    @Test
    void sendMessageC() {
        HttpResponse<String> response = client.toBlocking().exchange("/sendC", String.class);
        Assertions.assertThat(response.code()).isEqualTo(200);
        Assertions.assertThat(response.body()).startsWith("k-micronaut-");

        ArgumentCaptor<TestCMessage> messageCaptor = ArgumentCaptor.forClass(TestCMessage.class);
        Mockito.verify(myKafkaAvroClient).sendMessageC(Mockito.startsWith("k-micronaut-"), messageCaptor.capture());

        TestCMessage testCMessage = messageCaptor.getValue();
        Assertions.assertThat(testCMessage).isNotNull();
        Assertions.assertThat(testCMessage.getId()).isPositive();
        Assertions.assertThat(testCMessage.getText()).isNotBlank();
        Assertions.assertThat(testCMessage.getTimestamp()).isPositive();
        Assertions.assertThat(testCMessage.getData()).isEqualTo("data");
    }

}
