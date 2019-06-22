package com.zendesk.codingchallenge.search.model;

import org.springframework.core.io.ClassPathResource;

public class TicketTest extends BasePojoTest<Ticket> {

    public TicketTest() {
        super(Ticket.class, new ClassPathResource("single_ticket.json"));
    }
}