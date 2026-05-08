package io.tfkfan;

import io.tfkfan.asyncapi.events.OrdersEventsProducer;
import io.tfkfan.asyncapi.events.PaymentsEventsProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;
import ru.argo.avro.Order;
import ru.argo.avro.Payment;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class Application implements CommandLineRunner {
    static final UUID senderId = UUID.fromString("5f2df706-00d1-4375-814d-5771f99b4ca4");

    private final OrdersEventsProducer ordersEventsProducer;
    private final PaymentsEventsProducer paymentsEventsProducer;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) {
        log.info("App started");

        final Payment p = Payment.newBuilder()
                .setFrom(senderId)
                .setTo(UUID.randomUUID())
                .setTransactionId(UUID.randomUUID())
                .setAmount(990.00)
                .build();

        final Order o = Order.newBuilder()
                .setTransactionId(p.getTransactionId())
                .setTitle(UUID.randomUUID())
                .build();

        paymentsEventsProducer.onPaymentCreated(p);
        ordersEventsProducer.onOrderCreated(o);
    }
}