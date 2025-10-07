package dev.tests.kafka.kafkamicronaut.controller;


import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {

    @Get(produces = MediaType.TEXT_PLAIN)
    public String home() {
        log.info("OK");
        return "OK";
    }

}
