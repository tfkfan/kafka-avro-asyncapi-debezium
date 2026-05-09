package io.tfkfan.outbox;

import lombok.Builder;
import lombok.Data;
import org.apache.avro.specific.SpecificRecord;

import java.util.UUID;

@Builder
@Data
public class OutboxEvent<A extends SpecificRecord> {
    private UUID id;
    private String topic;
    private A payload;
}
