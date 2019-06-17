package com.zendesk.codingchallenge.search.repository;

import java.util.List;

/**
 * A generic repository that is in charge of loading all the entities, regardless where they come from
 *
 * @param <T> the entity type
 */
public interface EntityRepository<T> {

    /**
     * Retrieves all the entities held in this repository
     *
     * @return the list of entities
     */
    List<T> findAll();

}
