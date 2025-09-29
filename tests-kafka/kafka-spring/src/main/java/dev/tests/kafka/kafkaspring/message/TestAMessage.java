package dev.tests.kafka.kafkaspring.message;

public class TestAMessage {

    private int id;

    private String text;

    public TestAMessage() {
    }

    public TestAMessage(int id, String text) {
        this.id = id;
        this.text = text;
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

}
