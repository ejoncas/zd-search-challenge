package com.zendesk.codingchallenge.search.commands;

import com.google.gson.GsonBuilder;
import com.zendesk.codingchallenge.search.model.BaseEntity;
import com.zendesk.codingchallenge.search.model.EntityType;
import com.zendesk.codingchallenge.search.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.Map;

@ShellComponent
public class SearchCommand extends BaseSearchCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchCommand.class);

    public SearchCommand(Map<EntityType, SearchService<? extends BaseEntity>> searchServiceMap) {
        super(searchServiceMap);
    }

    @ShellMethod("Search for Tickets, Users or Organizations")
    public String search(
            @ShellOption(help = "Type to search, can be Ticket, User or Organization") String type,
            @ShellOption(help = "Field to search, for more info see the showfields commands") String field,
            @ShellOption(help = "Value to search for, full word match only") String value) {
        EntityType entityType = EntityType.fromName(type);
        SearchService<?> searchService = getSearchService(entityType);
        if (!searchService.getIndexedFields().contains(field)) {
            throw new IllegalArgumentException("Field '" + field + "' is invalid for entity type '" + type + "'");
        }
        List<? extends BaseEntity> search = searchService.search(field, value);
        return renderResults(search);
    }

    private String renderResults(List<? extends BaseEntity> search) {
        //TODO have a nicer render
        return new GsonBuilder().setPrettyPrinting().create().toJson(search);
    }

}
