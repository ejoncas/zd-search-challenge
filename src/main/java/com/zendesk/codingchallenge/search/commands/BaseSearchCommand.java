package com.zendesk.codingchallenge.search.commands;

import com.zendesk.codingchallenge.search.model.BaseEntity;
import com.zendesk.codingchallenge.search.model.EntityType;
import com.zendesk.codingchallenge.search.service.SearchService;

import java.util.Map;

public abstract class BaseSearchCommand {

    private final Map<EntityType, SearchService<? extends BaseEntity>> searchServiceMap;

    protected BaseSearchCommand(Map<EntityType, SearchService<? extends BaseEntity>> searchServiceMap) {
        this.searchServiceMap = searchServiceMap;
    }

    protected SearchService<?> getSearchService(EntityType entityType) {
        SearchService<?> searchService = searchServiceMap.get(entityType);
        if (searchService == null) {
            throw new IllegalArgumentException("No search service configured for '" + entityType + "'");
        }
        return searchService;
    }

    protected Map<EntityType, SearchService<? extends BaseEntity>> getSearchServiceMap() {
        return searchServiceMap;
    }
}
