package io.tfkfan.config;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class AvroConfiguration {
    @Bean
    public KafkaAvroDeserializer kafkaAvroDeserializer() {
        final KafkaAvroDeserializer deserializer = new KafkaAvroDeserializer();
        deserializer.configure(Map.of("schema.registry.url", "http://localhost:8081"), false);
        return deserializer;
    }

    @Bean
    public KafkaAvroSerializer kafkaAvroSerializer() {
        final KafkaAvroSerializer serializer = new KafkaAvroSerializer();
        serializer.configure(Map.of("schema.registry.url", "http://localhost:8081"), false);
        return serializer;
    }
}
