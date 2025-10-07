package dev.tests.kafka.kafkamicronaut.message;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

@Data
@Serdeable
public class TestAMessage {

    private int id;

    private String text;

    private String data;

    public TestAMessage() {
    }

    public TestAMessage(int id, String text) {
        this.id = id;
        this.text = text;
        this.data = "data";
    }

}
