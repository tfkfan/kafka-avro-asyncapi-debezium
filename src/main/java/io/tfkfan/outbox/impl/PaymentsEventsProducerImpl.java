package io.tfkfan.outbox.impl;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.tfkfan.asyncapi.events.DefaultPaymentsEventsProducer;
import io.tfkfan.outbox.OutboxEvent;
import io.tfkfan.outbox.OutboxEventProducer;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.argo.avro.Payment;

@Primary
@Component
public class PaymentsEventsProducerImpl extends DefaultPaymentsEventsProducer {
    private final OutboxEventProducer<Payment> eventProducer;

    public PaymentsEventsProducerImpl(JdbcTemplate jdbcTemplate, KafkaAvroSerializer avroSerializer) {
        super(null, null);
        eventProducer = new OutboxEventProducer<>("payments_outbox", null, jdbcTemplate, avroSerializer);
    }

    @Override
    public boolean onPaymentCreated(Payment payload, PaymentHeaders headers) {
        return eventProducer.send(OutboxEvent
                .<Payment>builder()
                .id(payload.getTransactionId())
                .topic(paymentsTopicName)
                .payload(payload)
                .build());
    }
}
