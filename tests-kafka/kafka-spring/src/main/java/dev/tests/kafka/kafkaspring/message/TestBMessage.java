package dev.tests.kafka.kafkaspring.message;

import lombok.Data;

@Data
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
