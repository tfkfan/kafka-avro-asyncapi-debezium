package io.tfkfan.outbox;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class OutboxEvent<A  > {
    private UUID id;
    private String topic;
    private A payload;
}
