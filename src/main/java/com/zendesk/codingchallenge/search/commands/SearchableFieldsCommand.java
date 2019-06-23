package com.zendesk.codingchallenge.search.commands;

import com.zendesk.codingchallenge.search.model.BaseEntity;
import com.zendesk.codingchallenge.search.model.EntityType;
import com.zendesk.codingchallenge.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Map;
import java.util.Set;

@ShellComponent
public class SearchableFieldsCommand extends BaseSearchCommand {

    @Autowired
    public SearchableFieldsCommand(Map<EntityType, SearchService<? extends BaseEntity>> searchServiceMap) {
        super(searchServiceMap);
    }

    @ShellMethod("List all searchable fields")
    public String showfields() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<EntityType, SearchService<? extends BaseEntity>> entry : getSearchServiceMap().entrySet()) {
            builder.append("Search " + entry.getKey() + " with:" + System.lineSeparator());
            Set<String> indexedFields = entry.getValue().getIndexedFields();
            for (String field : indexedFields) {
                builder.append(field + NEW_LINE);
            }
            builder.append(NEW_LINE);
        }
        return builder.toString();
    }

}
