package com.zendesk.codingchallenge.search.commands;

import com.zendesk.codingchallenge.search.commands.output.ModelRenderer;
import com.zendesk.codingchallenge.search.exception.SearchCommandFailedException;
import com.zendesk.codingchallenge.search.model.BaseEntity;
import com.zendesk.codingchallenge.search.model.EntityType;
import com.zendesk.codingchallenge.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.Map;

@ShellComponent
public class SearchCommand extends BaseSearchCommand {

    private final ModelRenderer modelRenderer;

    @Autowired
    public SearchCommand(Map<EntityType, SearchService<? extends BaseEntity>> searchServiceMap,
                         ModelRenderer modelRenderer) {
        super(searchServiceMap);
        this.modelRenderer = modelRenderer;
    }

    @ShellMethod("Search for Tickets, Users or Organizations")
    public String search(
            @ShellOption(help = "Type to search, can be Ticket, User or Organization") String type,
            @ShellOption(help = "Field to search, for more info see the showfields commands") String field,
            @ShellOption(help = "Value to search for, full word match only") String value) {
        EntityType entityType = EntityType.fromName(type);
        SearchService<?> searchService = getSearchService(entityType);
        if (!searchService.getIndexedFields().contains(field)) {
            throw new SearchCommandFailedException("Field '" + field + "' is invalid for entity type '" + type + "'");
        }
        List<? extends BaseEntity> search = searchService.search(field, value);
        return renderResults(search);
    }

    private <T> String renderResults(List<T> search) {
        if (search.isEmpty()) {
            return "No results found!";
        }
        StringBuilder builder = new StringBuilder();
        int resultNumber = 0;
        for (T searchResult : search) {
            builder.append("### Result Number ")
                    .append(++resultNumber)
                    .append(NEW_LINE)
                    .append(modelRenderer.render(searchResult))
                    .append(NEW_LINE)
                    .append(NEW_LINE);
        }
        return builder.toString();
    }

}
