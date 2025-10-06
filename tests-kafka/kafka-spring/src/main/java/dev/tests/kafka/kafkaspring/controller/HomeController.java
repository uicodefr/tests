package dev.tests.kafka.kafkaspring.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class HomeController {

    @GetMapping
    public String home() {
        log.info("OK");
        return "OK";
    }

}
