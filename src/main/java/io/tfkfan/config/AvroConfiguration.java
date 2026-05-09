package io.tfkfan.config;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class AvroConfiguration {
    private final String schemaRegistryUrl;

    public AvroConfiguration(@Value("${spring.kafka.producer.properties.schema.registry.url:http://localhost:8081}") String schemaRegistryUrl) {
        this.schemaRegistryUrl = schemaRegistryUrl;
    }

    @Bean
    public KafkaAvroDeserializer kafkaAvroDeserializer() {
        final KafkaAvroDeserializer deserializer = new KafkaAvroDeserializer();
        deserializer.configure(Map.of("schema.registry.url", schemaRegistryUrl), false);
        return deserializer;
    }

    @Bean
    public KafkaAvroSerializer kafkaAvroSerializer() {
        final KafkaAvroSerializer serializer = new KafkaAvroSerializer();
        serializer.configure(Map.of("schema.registry.url", schemaRegistryUrl), false);
        return serializer;
    }
}
