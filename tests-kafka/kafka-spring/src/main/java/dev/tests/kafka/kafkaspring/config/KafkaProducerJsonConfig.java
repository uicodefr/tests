package dev.tests.kafka.kafkaspring.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import dev.tests.kafka.kafkaspring.message.TestAMessage;
import dev.tests.kafka.kafkaspring.message.TestBMessage;

@Configuration
public class KafkaProducerJsonConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    private <T> ProducerFactory<String, T> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    KafkaTemplate<String, TestAMessage> kafkaTemplateTestA() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    KafkaTemplate<String, TestBMessage> kafkaTemplateTestB() {
        return new KafkaTemplate<>(producerFactory());
    }

}
