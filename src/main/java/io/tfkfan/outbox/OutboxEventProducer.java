package io.tfkfan.outbox;

import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Objects;

@RequiredArgsConstructor
public class OutboxEventProducer<A> {
    private final String tableName;
    private final String defaultTopic;
    private final JdbcTemplate jdbcTemplate;
    private final KafkaJsonSchemaSerializer<A> serializer;

    public boolean send(OutboxEvent<A> event) {
        Objects.requireNonNull(event);
        Objects.requireNonNull(event.getId(), "id cannot be null");
        Objects.requireNonNull(event.getPayload(), "payload cannot be null");

        final String topic = event.getTopic() != null ? event.getTopic() : defaultTopic;
        Objects.requireNonNull(topic);

        int r1 = jdbcTemplate.update("INSERT INTO %s (id, topic, payload) VALUES (?, ?, ?)".formatted(tableName),
                event.getId(),
                topic,
                serializer.serialize(topic, event.getPayload()));

        return r1 > 0;
    }
}
