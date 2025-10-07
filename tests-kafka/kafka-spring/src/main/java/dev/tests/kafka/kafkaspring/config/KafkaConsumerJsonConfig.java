package dev.tests.kafka.kafkaspring.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import dev.tests.kafka.kafkaspring.message.TestBMessage;

@EnableKafka
@Configuration
public class KafkaConsumerJsonConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    DefaultKafkaConsumerFactory<String, TestBMessage> consumerJsonFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstants.GROUP_ID);

        JsonDeserializer<TestBMessage> jsonDeserializer = new JsonDeserializer<>(TestBMessage.class, false);
        jsonDeserializer.addTrustedPackages("dev.tests.kafka.kafkaspring.message");
        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, TestBMessage> kafkaListenerContainerFactory(
            ConsumerFactory<String, TestBMessage> consumerJsonFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, TestBMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerJsonFactory);
        factory.setAutoStartup(true);
        factory.setConcurrency(2);
        return factory;
    }

}
