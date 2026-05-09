package io.tfkfan.outbox.impl;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.tfkfan.asyncapi.events.DefaultOrdersEventsProducer;
import io.tfkfan.outbox.OutboxEvent;
import io.tfkfan.outbox.OutboxEventProducer;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.argo.avro.Order;

@Primary
@Component
public class OrdersEventsProducerImpl extends DefaultOrdersEventsProducer {
    private final OutboxEventProducer<Order> eventProducer;

    public OrdersEventsProducerImpl(JdbcTemplate jdbcTemplate, KafkaAvroSerializer avroSerializer) {
        super(null, null);
        eventProducer = new OutboxEventProducer<>("orders_outbox", null, jdbcTemplate, avroSerializer);
    }

    @Override
    public boolean onOrderCreated(Order payload, OrderHeaders headers) {
        return eventProducer.send(OutboxEvent
                .<Order>builder()
                .id(payload.getTransactionId())
                .topic(ordersTopicName)
                .payload(payload)
                .build());
    }
}
