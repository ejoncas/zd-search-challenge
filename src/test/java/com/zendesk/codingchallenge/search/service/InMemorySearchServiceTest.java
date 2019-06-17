package com.zendesk.codingchallenge.search.service;

import com.google.common.collect.ImmutableSet;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import com.zendesk.codingchallenge.search.config.SearchConfig;
import com.zendesk.codingchallenge.search.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(DataProviderRunner.class)
public class InMemorySearchServiceTest {

    @DataProvider
    public static Object[][] userScenarios() {
        SearchConfig config = new SearchConfig();
        List<User> users = config.userRepository().findAll();
        return new Object[][]{
                {users, "name", "Watkins", ImmutableSet.of(12)},
                {users, "email", "flotonic", ImmutableSet.of(12)},
        };
    }

    @UseDataProvider("userScenarios")
    @Test
    public void testSearchUsers(List<User> users, String field, String word,
                                Set<Integer> expectedIds) {

        //Given
        InMemorySearchService<User> searchService = new InMemorySearchService(users);
        searchService.index();

        //When
        List<User> results = searchService.search("name", "Watkins");

        //Then
        Set<Integer> matchedEntitiesId = results.stream().map(User::getId).collect(toSet());
        assertThat(matchedEntitiesId, is(expectedIds));
    }

}