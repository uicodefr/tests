package dev.tests.kafka.kafkaspring.message;

import lombok.Data;

@Data
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
