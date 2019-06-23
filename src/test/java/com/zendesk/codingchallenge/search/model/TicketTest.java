package com.zendesk.codingchallenge.search.model;

import com.zendesk.codingchallenge.search.TestUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TicketTest extends BasePojoTest<Ticket> {

    public TicketTest() {
        super(Ticket.class, new ClassPathResource("single_ticket.json"));
    }

    @Test
    public void testPojo() {
        //Given
        Ticket ticket1 = TestUtils.sampleTicket();
        ticket1.setId(UUID.randomUUID());
        Ticket ticket2 = TestUtils.sampleTicket();
        ticket2.setId(ticket1.getId());
        Ticket ticket3 = TestUtils.sampleTicket();
        ticket3.setId(UUID.randomUUID());

        //Then
        assertEquals(ticket1, ticket2);
        assertNotEquals(ticket2, ticket3);
    }
}