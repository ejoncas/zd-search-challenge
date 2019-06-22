package com.zendesk.codingchallenge.search.service;

import com.zendesk.codingchallenge.search.model.BaseEntity;

import java.util.List;
import java.util.Set;

/**
 * A generic search service.
 *
 * @param <T> the type backed by this search service
 */
public interface SearchService<T extends BaseEntity<?>> {

    /**
     * Runs the index generation. This is a prerequesite for this search service to start returning results
     */
    void index();

    /**
     * Returns a set of all the fields indexed by this service.
     *
     * @return the set of fields
     */
    Set<String> getIndexedFields();

    /**
     * Returns a list of results matching the search criteria
     *
     * @param searchTerm the term to search for
     * @return the list of results
     */
    List<T> search(String field, String searchTerm);

}
