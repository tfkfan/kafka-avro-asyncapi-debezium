package io.tfkfan.outbox;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Objects;

@RequiredArgsConstructor
public class OutboxEventProducer<A extends SpecificRecord> {
    private final String tableName;
    private final String defaultTopic;
    private final JdbcTemplate jdbcTemplate;
    private final KafkaAvroSerializer avroSerializer;

    public boolean send(OutboxEvent<A> event) {
        Objects.requireNonNull(event);
        Objects.requireNonNull(event.getId(), "id cannot be null");
        Objects.requireNonNull(event.getPayload(), "payload cannot be null");
        Objects.requireNonNull(event.getEventType(), "eventType cannot be null");

        final String topic = event.getTopic() != null ? event.getTopic() : defaultTopic;
        Objects.requireNonNull(topic);

        return jdbcTemplate.update("INSERT INTO %s (id, topic, event_type, payload) VALUES (?, ?, ?, ?)".formatted(tableName),
                event.getId(),
                topic,
                event.getEventType().name(),
                avroSerializer.serialize(topic, event.getPayload())) > 0;
    }
}
