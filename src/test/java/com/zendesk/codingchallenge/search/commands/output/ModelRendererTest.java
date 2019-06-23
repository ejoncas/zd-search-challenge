package com.zendesk.codingchallenge.search.commands.output;

import com.google.common.collect.ImmutableList;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import com.zendesk.codingchallenge.search.TestUtils;
import com.zendesk.codingchallenge.search.model.Organisation;
import com.zendesk.codingchallenge.search.model.Ticket;
import com.zendesk.codingchallenge.search.model.User;
import com.zendesk.codingchallenge.search.service.SearchService;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RunWith(DataProviderRunner.class)
public class ModelRendererTest {

    @Mock
    private SearchService<User> userSearch;
    @Mock
    private SearchService<Organisation> organisationSearch;
    @Mock
    private SearchService<Ticket> ticketSearch;

    private ModelRenderer modelRenderer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        modelRenderer = new ModelRenderer(
                userSearch, organisationSearch, ticketSearch
        );
        //Mock responses
        doReturn(ImmutableList.of(TestUtils.sampleUser())).when(userSearch).search(anyString(), anyString());
        doReturn(ImmutableList.of(TestUtils.sampleOrganisation())).when(organisationSearch).search(anyString(), anyString());
        doReturn(ImmutableList.of(TestUtils.sampleTicket())).when(ticketSearch).search(anyString(), anyString());
    }

    @DataProvider
    public static Object[][] renderScenarios() {
        User user = TestUtils.sampleUser();
        Ticket ticket = TestUtils.sampleTicket();
        Organisation organisation = TestUtils.sampleOrganisation();
        //Assertions here just make sure that the id of the item is written to the result. Format is not validated
        //by this class. That is being done in TableOutputTest
        return new Object[][]{
                {"random object", equalTo("random object")},
                {new BigDecimal("1234"), equalTo("1234")},
                {user, containsString(user.getId().toString())},
                {ticket, containsString(ticket.getId().toString())},
                {organisation, containsString(organisation.getId().toString())},
        };
    }

    @UseDataProvider("renderScenarios")
    @Test
    public void testRender(Object objectToRender, Matcher<String> expectedResult) {
        assertThat(modelRenderer.render(objectToRender), is(expectedResult));
    }


}