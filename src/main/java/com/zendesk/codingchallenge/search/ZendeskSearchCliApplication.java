package com.zendesk.codingchallenge.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ZendeskSearchCliApplication {

    private static Logger LOGGER = LoggerFactory.getLogger(ZendeskSearchCliApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ZendeskSearchCliApplication.class, args);
    }

}
