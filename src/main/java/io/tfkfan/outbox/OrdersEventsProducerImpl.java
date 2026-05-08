package io.tfkfan.outbox;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.tfkfan.asyncapi.events.DefaultOrdersEventsProducer;
import io.tfkfan.asyncapi.events.OrdersEventsProducer;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.argo.avro.Order;
import ru.argo.avro.Payment;

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
                .eventType(OutboxEventType.CREATE)
                .payload(payload)
                .build());
    }
}
