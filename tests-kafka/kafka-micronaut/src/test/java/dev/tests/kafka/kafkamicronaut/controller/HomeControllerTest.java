package dev.tests.kafka.kafkamicronaut.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
class HomeControllerTest {

    @Inject
    @Client("/")
    private HttpClient client;


    @Test
    void home() {
        HttpResponse<String> response = client.toBlocking().exchange("/", String.class);
        Assertions.assertThat(response.code()).isEqualTo(200);
        Assertions.assertThat(response.body()).isEqualTo("OK");
    }

}
