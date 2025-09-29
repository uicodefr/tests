package dev.tests.kafka.kafkamicronaut.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    
    @Get(produces = MediaType.TEXT_PLAIN)
    public String home() {
        logger.info("OK");
        return "OK";
    }
    
}
