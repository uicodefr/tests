package dev.tests.kafka.kafkamicronaut;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
class KafkaMicronautTest {

    @Inject
    EmbeddedApplication<?> application;

    @Test
    void testItWorks() {
        // The application start in test without errors
        Assertions.assertThat(application.isRunning()).isTrue();
    }

}
