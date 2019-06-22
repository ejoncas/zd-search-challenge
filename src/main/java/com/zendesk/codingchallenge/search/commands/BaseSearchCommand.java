package com.zendesk.codingchallenge.search.commands;

import com.zendesk.codingchallenge.search.exception.SearchCommandFailedException;
import com.zendesk.codingchallenge.search.model.BaseEntity;
import com.zendesk.codingchallenge.search.model.EntityType;
import com.zendesk.codingchallenge.search.service.SearchService;

import java.util.Map;

public abstract class BaseSearchCommand {

    protected static final String NEW_LINE = System.lineSeparator();
    private final Map<EntityType, SearchService<? extends BaseEntity>> searchServiceMap;

    protected BaseSearchCommand(Map<EntityType, SearchService<? extends BaseEntity>> searchServiceMap) {
        this.searchServiceMap = searchServiceMap;
    }

    protected SearchService<?> getSearchService(EntityType entityType) {
        SearchService<?> searchService = searchServiceMap.get(entityType);
        if (searchService == null) {
            throw new SearchCommandFailedException("No search service configured for '" + entityType + "'");
        }
        return searchService;
    }

    protected Map<EntityType, SearchService<? extends BaseEntity>> getSearchServiceMap() {
        return searchServiceMap;
    }
}
