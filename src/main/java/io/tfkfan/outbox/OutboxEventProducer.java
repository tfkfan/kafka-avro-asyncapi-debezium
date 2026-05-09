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

        final String topic = event.getTopic() != null ? event.getTopic() : defaultTopic;
        Objects.requireNonNull(topic);

        int r1 = jdbcTemplate.update("INSERT INTO %s (id, topic, payload) VALUES (?, ?, ?)".formatted(tableName),
                event.getId(),
                topic,
                avroSerializer.serialize(topic, event.getPayload()));
        int r2 = jdbcTemplate.update("DELETE FROM %s WHERE id=?".formatted(tableName),
                event.getId());
        return r1 > 0 && r2 > 0;
    }
}
