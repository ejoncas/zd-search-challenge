package com.zendesk.codingchallenge.search;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.zendesk.codingchallenge.search.config.SearchConfig;
import com.zendesk.codingchallenge.search.model.BaseEntity;
import com.zendesk.codingchallenge.search.model.Organisation;
import com.zendesk.codingchallenge.search.model.Ticket;
import com.zendesk.codingchallenge.search.model.User;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang.math.RandomUtils.nextBoolean;
import static org.apache.commons.lang.math.RandomUtils.nextInt;

public class TestUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);
    private static final int NUMBER_OF_USERS_IN_FILE = 400_000;
    private static final Gson GSON = new SearchConfig().gson();

    /**
     * This file has been used to create really big files for testing.
     * I'm not including any file in the  repo as they  were huge.
     * <p>
     * This test is ignored to not generate huge files when you run the build.
     * <p>
     * If needed, it can be un-ignored and it will generate a JSON in your temp folder
     *
     * @throws IOException if something goes wrong while writing the file
     */
    @Test
    @Ignore
    public void generateBiggerFilesForTesting() throws IOException {
        //Get a sample
        List<User> newOnes = Lists.newArrayList();

        for (int i = 0; i < NUMBER_OF_USERS_IN_FILE; i++) {
            //Add some randomness to make sure we get lots of new words to be indexed.
            User deepCopy = sampleUser();
            newOnes.add(deepCopy);
        }
        File tempFile = File.createTempFile("user", ".json");
        try (FileWriter writer = new FileWriter(tempFile)) {
            GSON.toJson(newOnes, writer);
        }
        LOGGER.info("Written to {}", tempFile);
    }


    public static User sampleUser() {
        return sampleUser(nextInt());
    }

    public static User sampleUser(int id) {
        User user = new User();
        user.setId(id);
        user.setName(randomAlphanumeric(10));
        user.setAlias(RandomStringUtils.randomAlphabetic(200));
        user.setShared(nextBoolean());
        user.setActive(nextBoolean());
        user.setVerified(nextBoolean());
        user.setTimezone(RandomStringUtils.randomAlphabetic(5));
        user.setLastLoginAt(OffsetDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        user.setLocale(RandomStringUtils.randomAlphabetic(5));
        user.setPhone(RandomStringUtils.randomNumeric(10));
        user.setSignature(RandomStringUtils.randomAlphabetic(10));
        user.setOrganizationId(nextInt());
        user.setSuspended(nextBoolean());
        user.setRole(randomAlphanumeric(10));
        user.setEmail(randomAlphanumeric(50));
        setBaseFields(user);
        return user;
    }

    public static Ticket sampleTicket() {
        return sampleTicket(UUID.randomUUID());
    }

    public static Ticket sampleTicket(UUID id) {
        Ticket ticket = new Ticket();
        ticket.setId(id);
        setBaseFields(ticket);
        ticket.setType(Ticket.Type.problem);
        ticket.setSubject(RandomStringUtils.randomAlphanumeric(10));
        ticket.setDescription(RandomStringUtils.randomAlphanumeric(10));
        ticket.setPriority(Ticket.Priority.high);
        ticket.setStatus(Ticket.Status.closed);
        ticket.setSubmitterId(nextInt());
        ticket.setAssigneeId(nextInt());
        ticket.setOrganizationId(nextInt());
        ticket.setHasIncidents(nextBoolean());
        ticket.setDueAt(OffsetDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        ticket.setVia(RandomStringUtils.randomAlphabetic(5));
        return ticket;
    }

    public static Organisation sampleOrganisation() {
        return sampleOrganisation(nextInt());
    }

    public static Organisation sampleOrganisation(int id) {
        Organisation organisation = new Organisation();
        organisation.setId(id);
        setBaseFields(organisation);
        organisation.setDomainNames(ImmutableList.of(
                randomAlphanumeric(10),
                randomAlphanumeric(20)
        ));
        organisation.setDetails(randomAlphanumeric(10));
        organisation.setSharedTickets(nextBoolean());
        organisation.setName(randomAlphanumeric(25));
        return organisation;
    }

    private static <T extends BaseEntity> void setBaseFields(T entity) {
        entity.setUrl(randomAlphanumeric(10));
        entity.setExternalId(UUID.randomUUID().toString());
        entity.setCreatedAt(OffsetDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        entity.setTags(ImmutableSortedSet.of(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(20)
        ));
    }
}
