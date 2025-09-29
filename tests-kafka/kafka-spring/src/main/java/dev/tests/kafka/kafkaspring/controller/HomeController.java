package dev.tests.kafka.kafkaspring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping
    public String home() {
        logger.info("OK");
        return "OK";
    }

}
