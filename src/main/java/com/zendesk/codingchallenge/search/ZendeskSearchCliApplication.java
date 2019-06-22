package com.zendesk.codingchallenge.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class
 */
@SpringBootApplication
public class ZendeskSearchCliApplication {

    /**
     * Application  entrypoint
     *
     * @param args arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(ZendeskSearchCliApplication.class, args);
    }

}
