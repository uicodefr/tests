package dev.tests.kafka.kafkamicronaut.message;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class TestBMessage {

    private int id;

    private String text;

    private long timestamp;

    public TestBMessage() {
    }

    public TestBMessage(int id, String text, long timestamp) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
