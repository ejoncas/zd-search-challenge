package com.zendesk.codingchallenge.search.commands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.zendesk.codingchallenge.search.TestUtils;
import com.zendesk.codingchallenge.search.commands.output.ModelRenderer;
import com.zendesk.codingchallenge.search.exception.SearchCommandFailedException;
import com.zendesk.codingchallenge.search.model.EntityType;
import com.zendesk.codingchallenge.search.model.Ticket;
import com.zendesk.codingchallenge.search.service.SearchService;
import com.zendesk.codingchallenge.search.utils.ResourceUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class SearchCommandTest {

    private List<Ticket> sampleTickets = ImmutableList.of(
            TestUtils.sampleTicket(UUID.fromString("8b11c778-1344-4cfd-87bc-16522e57faa9")),
            TestUtils.sampleTicket(UUID.fromString("6e89338a-f4cd-426f-b143-ca7ea1233ccf"))
    );

    @Mock
    private SearchService<Ticket> searchService;
    @Mock
    private ModelRenderer modelRenderer;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    //Subject
    private SearchCommand command;

    @Before
    public void setup() {
        //Simple model Renderer returns object toString
        doAnswer(answer -> answer.getArgument(0).toString()).when(modelRenderer).render((Object) Mockito.any());
        doReturn(sampleTickets).when(searchService).search(anyString(), anyString());
        doReturn(ImmutableSet.of("supportedField")).when(searchService).getIndexedFields();
        this.command = new SearchCommand(
                ImmutableMap.of(
                        EntityType.TICKET, searchService
                ), modelRenderer
        );
    }

    @Test
    public void testHappyPath() throws IOException {
        //When
        String searchOutput = this.command.search(EntityType.TICKET.getName(), "supportedField", "expectedValue");

        //Then
        assertThat(searchOutput, is(ResourceUtils.resourceToString("search_ticket_expected.txt")));
    }

    @Test
    public void testInvalidEntityType() {
        expectedException.expect(SearchCommandFailedException.class);
        expectedException.expectMessage("is not a valid entity type. ");
        expectedException.expectMessage("Accepted values are: [ticket, organisation, user]");
        this.command.search("NotAKnownEntity", "field", "expectedValue");
    }

    @Test
    public void testNonConfiguredType() {
        expectedException.expect(SearchCommandFailedException.class);
        expectedException.expectMessage("No search service configured for 'USER'");
        this.command.search(EntityType.USER.getName(), "field", "expectedValue");
    }

    @Test
    public void testInvalidField() {
        expectedException.expect(SearchCommandFailedException.class);
        expectedException.expectMessage("Field 'nonSupportedField' is invalid for entity type 'ticket'");
        this.command.search(EntityType.TICKET.getName(), "nonSupportedField", "expectedValue");
    }
}