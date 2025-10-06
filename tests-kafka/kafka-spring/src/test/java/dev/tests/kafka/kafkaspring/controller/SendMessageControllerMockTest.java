package dev.tests.kafka.kafkaspring.controller;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import dev.tests.kafka.kafkaspring.avro.TestCMessage;
import dev.tests.kafka.kafkaspring.config.KafkaConstants;
import dev.tests.kafka.kafkaspring.message.TestAMessage;
import dev.tests.kafka.kafkaspring.message.TestBMessage;

@WebMvcTest(SendMessageController.class)
class SendMessageControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KafkaTemplate<String, TestAMessage> kafkaTemplateA;

    @MockitoBean
    private KafkaTemplate<String, TestBMessage> kafkaTemplateB;

    @MockitoBean
    private KafkaTemplate<String, TestCMessage> kafkaTemplateC;


    @Test
    void sendMessageA() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/sendA"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(Matchers.startsWith("k-spring-")));

        ArgumentCaptor<TestAMessage> messageCaptor = ArgumentCaptor.forClass(TestAMessage.class);
        Mockito.verify(kafkaTemplateA).send(Mockito.eq(KafkaConstants.TOPIC_A), Mockito.startsWith("k-spring-"), messageCaptor.capture());

        TestAMessage testAMessage = messageCaptor.getValue();
        Assertions.assertThat(testAMessage).isNotNull();
        Assertions.assertThat(testAMessage.getId()).isPositive();
        Assertions.assertThat(testAMessage.getText()).isNotBlank();
        Assertions.assertThat(testAMessage.getData()).isEqualTo("data");
    }

    @Test
    void sendMessageB() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/sendB"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(Matchers.startsWith("k-spring-")));

        ArgumentCaptor<TestBMessage> messageCaptor = ArgumentCaptor.forClass(TestBMessage.class);
        Mockito.verify(kafkaTemplateB).send(Mockito.eq(KafkaConstants.TOPIC_B), Mockito.startsWith("k-spring-"), messageCaptor.capture());

        TestBMessage testBMessage = messageCaptor.getValue();
        Assertions.assertThat(testBMessage).isNotNull();
        Assertions.assertThat(testBMessage.getId()).isPositive();
        Assertions.assertThat(testBMessage.getText()).isNotBlank();
        Assertions.assertThat(testBMessage.getTimestamp()).isPositive();
        Assertions.assertThat(testBMessage.getData()).isEqualTo("data");
    }

    @Test
    void sendMessageC() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/sendC"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(Matchers.startsWith("k-spring-")));

        ArgumentCaptor<TestCMessage> messageCaptor = ArgumentCaptor.forClass(TestCMessage.class);
        Mockito.verify(kafkaTemplateC).send(Mockito.eq(KafkaConstants.TOPIC_C), Mockito.startsWith("k-spring-"), messageCaptor.capture());

        TestCMessage testCMessage = messageCaptor.getValue();
        Assertions.assertThat(testCMessage).isNotNull();
        Assertions.assertThat(testCMessage.getId()).isPositive();
        Assertions.assertThat(testCMessage.getText()).isNotBlank();
        Assertions.assertThat(testCMessage.getTimestamp()).isPositive();
        Assertions.assertThat(testCMessage.getExtra()).isEqualTo(List.of("abc", "def"));
        Assertions.assertThat(testCMessage.getData()).isEqualTo("data");
    }

}
