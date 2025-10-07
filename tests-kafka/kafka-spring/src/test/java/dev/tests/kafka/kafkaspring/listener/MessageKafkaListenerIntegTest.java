package dev.tests.kafka.kafkaspring.listener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.avro.util.Utf8;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import dev.tests.kafka.kafkaspring.avro.TestCMessage;
import dev.tests.kafka.kafkaspring.config.KafkaConstants;
import dev.tests.kafka.kafkaspring.message.TestBMessage;
import dev.tests.kafka.kafkaspring.service.FakeService;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@EmbeddedKafka
@Slf4j
class MessageKafkaListenerIntegTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerRegistry; 

    @Value("${myapp.kafka.schema-registry}")
    private String schemaRegistry;

    @MockitoBean
    private FakeService fakeService;


    @BeforeEach
    void setup() {
        // Wait All Spring Consumers are ready
        kafkaListenerRegistry.getAllListenerContainers().forEach(listener -> {
            ContainerTestUtils.waitForAssignment(listener, embeddedKafkaBroker.getPartitionsPerTopic());
        });
    }

    private <T> Producer<String, T> makeJsonProducer() {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<String, T>(producerProps).createProducer();
    }

    @Test
    void listenTopicB() throws InterruptedException, ExecutionException {
        Producer<String, TestBMessage> producer = makeJsonProducer();

        String key = "k-test-1";
        TestBMessage sendMessage = new TestBMessage(123, "text", 1l);
        producer.send(new ProducerRecord<>(KafkaConstants.TOPIC_B, key, sendMessage)).get();

        ArgumentCaptor<TestBMessage> messageCaptor = ArgumentCaptor.forClass(TestBMessage.class);
        Awaitility.await().untilAsserted(() -> {
            Mockito.verify(fakeService).merge(Mockito.eq(key), messageCaptor.capture());
        });
        producer.close();

        TestBMessage testBMessage = messageCaptor.getValue();
        Assertions.assertThat(testBMessage).isNotNull();
        Assertions.assertThat(testBMessage.getId()).isEqualTo(sendMessage.getId());
        Assertions.assertThat(testBMessage.getText()).isEqualTo(sendMessage.getText());
        Assertions.assertThat(testBMessage.getTimestamp()).isEqualTo(sendMessage.getTimestamp());
        Assertions.assertThat(testBMessage.getData()).isEqualTo(sendMessage.getData());
    }

    private <T> Producer<String, T> makeAvroProducer() {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        producerProps.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);

        return new DefaultKafkaProducerFactory<String, T>(producerProps).createProducer();
    }

    @Test
    void listenTopicC() throws InterruptedException, ExecutionException {
        Producer<String, TestCMessage> producer = makeAvroProducer();

        String key = "k-test-1";
        TestCMessage sendMessage = new TestCMessage(123, "text", 1l, List.of(new Utf8("abc"), new Utf8("def")), new Utf8("data"));
        producer.send(new ProducerRecord<>(KafkaConstants.TOPIC_C, key, sendMessage)).get();

        ArgumentCaptor<TestCMessage> messageCaptor = ArgumentCaptor.forClass(TestCMessage.class);
        Awaitility.await().untilAsserted(() -> {
            Mockito.verify(fakeService).merge(Mockito.eq(key), messageCaptor.capture());
        });
        producer.close();

        TestCMessage testCMessage = messageCaptor.getValue();
        Assertions.assertThat(testCMessage).isNotNull();
        Assertions.assertThat(testCMessage.getId()).isEqualTo(sendMessage.getId());
        Assertions.assertThat(testCMessage.getText()).isEqualTo(new Utf8(sendMessage.getText().toString()));
        Assertions.assertThat(testCMessage.getTimestamp()).isEqualTo(sendMessage.getTimestamp());
        Assertions.assertThat(testCMessage.getExtra()).hasSameElementsAs(sendMessage.getExtra());
        Assertions.assertThat(testCMessage.getData()).isEqualTo(sendMessage.getData());
    }

}
