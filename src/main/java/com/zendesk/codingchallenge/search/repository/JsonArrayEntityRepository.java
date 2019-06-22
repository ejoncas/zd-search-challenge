package com.zendesk.codingchallenge.search.repository;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zendesk.codingchallenge.search.exception.SearchCommandFailedException;
import com.zendesk.codingchallenge.search.model.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.zendesk.codingchallenge.search.utils.ResourceUtils.resourceToString;

/**
 * Load entitiesMap from a json array file
 *
 * @param <T> the type of the entitiesMap.
 */
public class JsonArrayEntityRepository<ID, T extends BaseEntity<ID>> implements EntityRepository<ID, T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonArrayEntityRepository.class);

    private Map<ID, T> entitiesMap = Maps.newHashMap();

    /**
     * Generic Repository that loads entities from a json file
     *
     * @param clazz              the type, this is useful for deserializing generics
     * @param repositoryResource the resource to load the entities from
     * @param gson               the gson utility
     */
    public JsonArrayEntityRepository(Class<T> clazz, ClassPathResource repositoryResource, Gson gson) {
        init(clazz, repositoryResource, gson);
    }

    /**
     * Loads the entities from the classpath resource
     */
    private void init(Class<T> clazz, ClassPathResource repositoryResource, Gson gson) {
        try {
            String data = resourceToString(repositoryResource);
            List<T> entities = gson.fromJson(data, TypeToken.getParameterized(List.class, clazz).getType());
            for (T entity : entities) {
                T entryValue = entitiesMap.get(entity.getId());
                if (entryValue == null) {
                    entitiesMap.put(entity.getId(), entity);
                } else {
                    throw new SearchCommandFailedException("Two entities with the same id! " + entryValue + " and " + entity);
                }
            }
            LOGGER.info("Loaded {} entities to {} repository", entities.size(), clazz.getSimpleName());
        } catch (IOException e) {
            throw new SearchCommandFailedException("Could not load file " + repositoryResource + " as entities", e);
        }

    }

    @Override
    public Collection<T> findAll() {
        return entitiesMap.values();
    }


    @Override
    public T findById(ID id) {
        return entitiesMap.get(id);
    }
}
