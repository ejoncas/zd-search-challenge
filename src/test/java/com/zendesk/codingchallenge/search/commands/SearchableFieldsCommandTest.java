package com.zendesk.codingchallenge.search.commands;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.zendesk.codingchallenge.search.model.EntityType;
import com.zendesk.codingchallenge.search.service.SearchService;
import com.zendesk.codingchallenge.search.utils.ResourceUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

public class SearchableFieldsCommandTest {

    private SearchableFieldsCommand command;

    @Before
    public void setup() {
        SearchService<?> searchService = Mockito.mock(SearchService.class);
        doReturn(ImmutableSet.of(
                "field1",
                "field2",
                "field3"
        )).when(searchService).getIndexedFields();
        this.command = new SearchableFieldsCommand(
                ImmutableMap.of(
                        EntityType.ORGANISATION, searchService,
                        EntityType.TICKET, searchService
                )
        );
    }

    @Test
    public void testOutputShouldShowFields() throws IOException {
        //When
        String result = this.command.showfields();
        //Then
        assertThat(result, is(equalTo(ResourceUtils.resourceToString(new ClassPathResource("show_fields_expected.txt")))));
    }


}