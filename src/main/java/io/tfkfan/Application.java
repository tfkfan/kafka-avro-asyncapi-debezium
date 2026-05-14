package io.tfkfan;

import io.tfkfan.asyncapi.events.Payment;
import io.tfkfan.outbox.OutboxEvent;
import io.tfkfan.outbox.OutboxEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class Application implements CommandLineRunner {
    static final UUID senderId = UUID.fromString("5f2df706-00d1-4375-814d-5771f99b4ca4");

    private final OutboxEventProducer<Payment> paymentsEventsProducer;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) {
        log.info("App script started");

        final Payment p = new Payment();
        p.setTransactionId(UUID.randomUUID().toString());
        p.setAmount(990.00);

        paymentsEventsProducer.send(OutboxEvent
                .<Payment>builder()
                .id(UUID.randomUUID())
                .topic("some_topic")
                .payload(p)
                .build());

        log.info("App script completed");
    }
}