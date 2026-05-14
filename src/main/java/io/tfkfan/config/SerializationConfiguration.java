package io.tfkfan.config;

import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer;
import io.tfkfan.asyncapi.events.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SerializationConfiguration {
    private final String schemaRegistryUrl;

    public SerializationConfiguration(@Value("${spring.kafka.producer.properties.schema.registry.url:http://localhost:8081}") String schemaRegistryUrl) {
        this.schemaRegistryUrl = schemaRegistryUrl;
    }

    @Bean
    public KafkaJsonSchemaDeserializer<Payment> kafkaDeserializer() {
        final KafkaJsonSchemaDeserializer<Payment> deserializer = new KafkaJsonSchemaDeserializer<>();
        deserializer.configure(Map.of("schema.registry.url", schemaRegistryUrl,
                "auto.register.schemas", true), false);
        return deserializer;
    }

    @Bean
    public KafkaJsonSchemaSerializer<Payment> kafkaSerializer() {
        final KafkaJsonSchemaSerializer<Payment> serializer = new KafkaJsonSchemaSerializer<>();
        serializer.configure(Map.of("schema.registry.url", schemaRegistryUrl,
                "auto.register.schemas", true), false);
        return serializer;
    }
}
