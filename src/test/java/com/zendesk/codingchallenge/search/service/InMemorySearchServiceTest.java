package com.zendesk.codingchallenge.search.service;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import com.zendesk.codingchallenge.search.config.SearchConfig;
import com.zendesk.codingchallenge.search.model.Organisation;
import com.zendesk.codingchallenge.search.model.Ticket;
import com.zendesk.codingchallenge.search.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.google.common.collect.ImmutableSet.of;
import static com.zendesk.codingchallenge.search.service.InMemorySearchService.NULL_KEYWORD;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(DataProviderRunner.class)
public class InMemorySearchServiceTest {

    private SearchService<User> userSearch;
    private SearchService<Ticket> ticketSearch;
    private SearchService<Organisation> organisationSearch;

    @Before
    public void setup() {
        SearchConfig config = new SearchConfig();

        userSearch = config.userSearchService();
        userSearch.index();

        ticketSearch = config.ticketSearchService();
        ticketSearch.index();

        organisationSearch = config.organisationSearchService();
        organisationSearch.index();
    }

    @DataProvider
    public static Object[][] userScenarios() {
        return new Object[][]{
                {"name", "Watkins", of(12)},
                {"email", "byersestrada@flotonic.com", of(15)},
                {"name", "Simpson", of(75, 1005)},
                {"tags", "Vicksburg", of(19)},
                {"tags", "Bascom", of(26)},
                {"email", NULL_KEYWORD, of(11, 19)},
                {"name", "Francis", of(19, 23)},
                {"organization_id", "119", of(1, 48, 73, 75, 1005)},
                {"timezone", "New", of(27, 34)},
                {"external_id", "e29c3611-d1f2-492e-a805-594e239ff922", of(66)},
                {"_id", "64", of(64)}
        };
    }

    @UseDataProvider("userScenarios")
    @Test
    public void testSearchUsers(String field, String word,
                                Set<Integer> expectedIds) {
        //When
        List<User> results = userSearch.search(field, word);

        //Then
        Set<Integer> matchedEntitiesId = results.stream().map(User::getId).collect(toSet());
        assertThat(matchedEntitiesId, is(expectedIds));
    }

    @DataProvider
    public static Object[][] organisationScenarios() {
        return new Object[][]{
                {"url", "http://initech.zendesk.com/api/v2/organizations/105.json", of(105)},
                {"tags", "Lester", of(123)},
                {"tags", "Erickson", of(119)},
                {"domain_names", "endipin.com", of(101)},
                {"shared_tickets", "true", of(107, 110, 111, 113, 114, 117, 121, 122, 123, 124)},
                {"details", "MegaCörp", of(104, 107, 113)},
                {"details", "profit", of(102, 103, 119, 122, 108, 124, 110)},
                {"external_id", "be72663b-338d-42f4-bd52-cf6584cad674", of(108)},
                {"_id", "101", of(101)}
        };
    }

    @UseDataProvider("organisationScenarios")
    @Test
    public void testSearchOrganisations(String field, String word,
                                        Set<Integer> expectedIds) {
        //When
        List<Organisation> results = organisationSearch.search(field, word);

        //Then
        Set<Integer> matchedEntitiesId = results.stream().map(Organisation::getId).collect(toSet());
        assertThat(matchedEntitiesId, is(expectedIds));
    }

    @DataProvider
    public static Object[][] ticketsScenarios() {
        return new Object[][]{
                {"_id", "436bf9b0-1147-4c0a-8439-6f79833bff5b", of(UUID.fromString("436bf9b0-1147-4c0a-8439-6f79833bff5b"))},
                {"url", "http://initech.zendesk.com/api/v2/tickets/436bf9b0-1147-4c0a-8439-6f79833bff5b.json",
                        of(UUID.fromString("436bf9b0-1147-4c0a-8439-6f79833bff5b"))},
                {"external_id", "c38e866e-0b2e-4207-a7da-bc4c2cb5eb8f", of(UUID.fromString("5aa53572-b31c-4d27-814b-11709ab00259"))},
                {"created_at", "2016-02-05T10:36:14", of(UUID.fromString("9a21f37a-8ac5-4ef1-8b99-f1d4ca9cf170"))},
                {"type", NULL_KEYWORD, of(UUID.fromString("8629d5fa-89c4-4e9b-9d9f-221b68b079f4"),
                        UUID.fromString("49a3526c-2bc4-45b0-a6dd-6a55e5a4bd9f"))},
                {"subject", "Virgin", of(UUID.fromString("b776f78f-e3ac-4139-9a8f-6f905472f44d"),
                        UUID.fromString("92c88581-f778-42bc-a828-0000afaa9588"))},
                {"description", "Proident", of(UUID.fromString("b4875dbc-c167-4625-a1e4-d14ed409c62c"),
                        UUID.fromString("a12a5f33-d4a0-4e43-8773-4b22e16fc0c8"))},
                {"priority", "nonExistent", of()},
                {"submitter_id", "1", of(UUID.fromString("fc5a8a70-3814-4b17-a6e9-583936fca909"),
                        UUID.fromString("cb304286-7064-4509-813e-edc36d57623d"))},
                {"assignee_id", "3", of(UUID.fromString("95870a6c-22bd-45c3-8d8e-b7f2c7d46b76"),
                        UUID.fromString("e75e6904-6536-43ea-9081-1c9f787f8682"))},
                {"organization_id", "555", of(UUID.fromString("7523607d-d45c-4e3a-93aa-419402e64d73"))},
                {"tags", "Rhodé", of(UUID.fromString("ec987652-c323-4368-899d-f3c357ff4b87"))},
                {"due_at", "2016-08-19T07:40:17", of(UUID.fromString("87db32c5-76a3-4069-954c-7d59c6c21de0"))}
        };
    }

    @UseDataProvider("ticketsScenarios")
    @Test
    public void testSearchTickets(String field, String word,
                                  Set<UUID> expectedIds) {
        //When
        List<Ticket> results = ticketSearch.search(field, word);

        //Then
        Set<UUID> matchedEntitiesId = results.stream().map(Ticket::getId).collect(toSet());
        assertThat(matchedEntitiesId, is(expectedIds));
    }

}