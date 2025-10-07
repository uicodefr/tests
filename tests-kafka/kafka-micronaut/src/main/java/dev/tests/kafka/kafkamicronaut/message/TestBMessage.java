package dev.tests.kafka.kafkamicronaut.message;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

@Data
@Serdeable
public class TestBMessage {

    private int id;

    private String text;

    private long timestamp;

    private String data;

    public TestBMessage() {
    }

    public TestBMessage(int id, String text, long timestamp) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
        this.data = "data";
    }

}
