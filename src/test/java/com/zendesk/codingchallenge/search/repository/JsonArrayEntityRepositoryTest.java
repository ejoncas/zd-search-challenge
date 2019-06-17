/*
 * Copyright (c) Message4U Pty Ltd 2014-2019
 *
 * Except as otherwise permitted by the Copyright Act 1967 (Cth) (as amended from time to time) and/or any other
 * applicable copyright legislation, the material may not be reproduced in any format and in any way whatsoever
 * without the prior written consent of the copyright owner.
 */

package com.zendesk.codingchallenge.search.repository;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import com.zendesk.codingchallenge.search.config.SearchConfig;
import com.zendesk.codingchallenge.search.model.Organisation;
import com.zendesk.codingchallenge.search.model.Ticket;
import com.zendesk.codingchallenge.search.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertThat;

@RunWith(DataProviderRunner.class)
public class JsonArrayEntityRepositoryTest {

    @DataProvider
    public static Object[][] loadEntitiesScenarios() {
        SearchConfig config = new SearchConfig();
        return new Object[][]{
                //input file, expected type, expected entities to load
                {config.organisationRepository(), Organisation.class, 25},
                {config.ticketRepository(), Ticket.class, 200},
                {config.userRepository(), User.class, 75},
        };
    }

    @Test
    @UseDataProvider("loadEntitiesScenarios")
    public <T> void testLoadingEntities(JsonArrayEntityRepository<T> repository, Class<T> clazz, int expectedEntities) {
        assertThat(repository.findAll(), hasSize(expectedEntities));
        for (T entity : repository.findAll()) {
            assertThat(entity, isA(clazz));
        }
    }

}