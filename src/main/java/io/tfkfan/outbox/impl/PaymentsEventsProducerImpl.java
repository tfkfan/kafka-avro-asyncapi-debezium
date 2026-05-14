package io.tfkfan.outbox.impl;

import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer;
import io.tfkfan.asyncapi.events.Payment;
import io.tfkfan.outbox.OutboxEventProducer;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Primary
@Component
public class PaymentsEventsProducerImpl extends OutboxEventProducer<Payment> {

    public PaymentsEventsProducerImpl(JdbcTemplate jdbcTemplate, KafkaJsonSchemaSerializer<Payment> serializer) {
        super("payments_outbox", null, jdbcTemplate, serializer);
    }
}
