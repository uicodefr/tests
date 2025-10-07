package dev.tests.kafka.kafkaspring.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.avro.util.Utf8;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import dev.tests.kafka.kafkaspring.avro.TestCMessage;
import dev.tests.kafka.kafkaspring.config.KafkaConstants;
import dev.tests.kafka.kafkaspring.message.TestAMessage;
import dev.tests.kafka.kafkaspring.message.TestBMessage;
import io.confluent.kafka.schemaregistry.avro.AvroSchema;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka
class SendMessageControllerIntegTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;


    private <T> Consumer<String, T> makeJsonConsumer(Class<T> messageClass, String topic) {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
        consumerProps.remove(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG);
        consumerProps.remove(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG);
        
        JsonDeserializer<T> jsonDeserializer = new JsonDeserializer<>(messageClass, false);
        jsonDeserializer.addTrustedPackages("dev.tests.kafka.kafkaspring.message");

        Consumer<String, T> consumer = new DefaultKafkaConsumerFactory<String, T>(consumerProps, new StringDeserializer(), jsonDeserializer).createConsumer();
        consumer.subscribe(Collections.singleton(topic));
        return consumer;
    }

    @Test
    void sendMessageA() throws Exception {
        Consumer<String, TestAMessage> consumer = makeJsonConsumer(TestAMessage.class, KafkaConstants.TOPIC_A);

        mockMvc.perform(MockMvcRequestBuilders.get("/sendA"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(Matchers.startsWith("k-spring-")));

        ConsumerRecord<String, TestAMessage> singleRecord = KafkaTestUtils.getSingleRecord(consumer, KafkaConstants.TOPIC_A);
        consumer.close();

        Assertions.assertThat(singleRecord.key()).startsWith("k-spring-");
        TestAMessage testAMessage = singleRecord.value();
        Assertions.assertThat(testAMessage.getId()).isPositive();
        Assertions.assertThat(testAMessage.getText()).isNotBlank();
        Assertions.assertThat(testAMessage.getData()).isEqualTo("data");
    }

    @Test
    void sendMessageB() throws Exception {
        Consumer<String, TestBMessage> consumer = makeJsonConsumer(TestBMessage.class, KafkaConstants.TOPIC_B);

        mockMvc.perform(MockMvcRequestBuilders.get("/sendB"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(Matchers.startsWith("k-spring-")));

        ConsumerRecord<String, TestBMessage> singleRecord = KafkaTestUtils.getSingleRecord(consumer, KafkaConstants.TOPIC_B);
        consumer.close();

        Assertions.assertThat(singleRecord.key()).startsWith("k-spring-");
        TestBMessage testBMessage = singleRecord.value();
        Assertions.assertThat(testBMessage.getId()).isPositive();
        Assertions.assertThat(testBMessage.getText()).isNotBlank();
        Assertions.assertThat(testBMessage.getTimestamp()).isPositive();
        Assertions.assertThat(testBMessage.getData()).isEqualTo("data");
    }

    private Consumer<String, Object> makeAvroConsumer(String topic) throws IOException, RestClientException {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
        consumerProps.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        consumerProps.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "mock://my-scope");
        consumerProps.remove(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG);
        consumerProps.remove(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG);

        SchemaRegistryClient mockRegistryClient = new MockSchemaRegistryClient();
        // Mock Schema Registry here, add all schema here
        mockRegistryClient.register(KafkaConstants.TOPIC_C + "-value", new AvroSchema(TestCMessage.SCHEMA$), 1, 1);

        KafkaAvroDeserializer avroDeserializer = new KafkaAvroDeserializer(mockRegistryClient);

        Consumer<String, Object> consumer = new DefaultKafkaConsumerFactory<String, Object>(consumerProps, new StringDeserializer(), avroDeserializer).createConsumer();
        consumer.subscribe(Collections.singleton(topic));
        return consumer;
    }

    @Test
    void sendMessageC() throws Exception {
        Consumer<String, Object> consumer = makeAvroConsumer(KafkaConstants.TOPIC_C);

        mockMvc.perform(MockMvcRequestBuilders.get("/sendC"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(Matchers.startsWith("k-spring-")));

        ConsumerRecord<String, Object> singleRecord = KafkaTestUtils.getSingleRecord(consumer, KafkaConstants.TOPIC_C);
        consumer.close();

        Assertions.assertThat(singleRecord.key()).startsWith("k-spring-");
        Assertions.assertThat(singleRecord.value()).isInstanceOf(TestCMessage.class);

        TestCMessage testCMessage = (TestCMessage)  singleRecord.value();
        Assertions.assertThat(testCMessage.getId()).isPositive();
        Assertions.assertThat(testCMessage.getText()).isNotBlank();
        Assertions.assertThat(testCMessage.getTimestamp()).isPositive();
        Assertions.assertThat(testCMessage.getExtra()).hasSameElementsAs(List.of(new Utf8("abc"), new Utf8("def")));
        Assertions.assertThat(testCMessage.getData()).isEqualTo(new Utf8("data"));
    }

}
