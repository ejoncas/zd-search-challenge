package com.zendesk.codingchallenge.search.repository;

import com.zendesk.codingchallenge.search.model.BaseEntity;

import java.util.Collection;

/**
 * A generic repository that is in charge of loading all the entities, regardless where they come from
 *
 * @param <T> the entity type
 */
public interface EntityRepository<ID, T extends BaseEntity<ID>> {

    /**
     * Retrieves all the entities held in this repository
     *
     * @return the list of entities
     */
    Collection<T> findAll();


    /**
     * Finds an entity by its id
     *
     * @param id the id
     * @return the entity
     */
    T findById(ID id);
}
