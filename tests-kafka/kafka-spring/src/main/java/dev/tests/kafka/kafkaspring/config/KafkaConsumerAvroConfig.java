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

import dev.tests.kafka.kafkaspring.avro.TestCMessage;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;

@EnableKafka
@Configuration
public class KafkaConsumerAvroConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${myapp.kafka.schema-registry}")
    private String schemaRegistry;

    @Bean
    DefaultKafkaConsumerFactory<String, Object> consumerAvroFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstants.GROUP_ID);
        configProps.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        configProps.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);

        KafkaAvroDeserializer avroDeserializer = new KafkaAvroDeserializer();
        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), avroDeserializer);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, TestCMessage> kafkaListenerContainerAvroFactory(
            ConsumerFactory<String, Object> consumerAvroFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, TestCMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerAvroFactory);
        factory.setAutoStartup(true);
        factory.setConcurrency(2);
        return factory;
    }

}
