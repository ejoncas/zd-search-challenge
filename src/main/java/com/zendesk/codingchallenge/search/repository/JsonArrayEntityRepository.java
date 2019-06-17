package com.zendesk.codingchallenge.search.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

public class JsonArrayEntityRepository<T> implements EntityRepository<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonArrayEntityRepository.class);

    private List<T> entities;

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
            entities = gson.fromJson(data, TypeToken.getParameterized(List.class, clazz).getType());
            LOGGER.info("Loaded {} entities to {} repository", entities.size(), clazz.getSimpleName());
        } catch (IOException e) {
            throw new UncheckedIOException("Could not load file " + repositoryResource + " as entities", e);
        }

    }

    /**
     * Reads the resource and returns the resource contents as a string.
     *
     * @param repositoryResource the  resource to load as a string
     * @return the file contents
     * @throws IOException if file does not exist or could not be  accessed
     */
    private String resourceToString(ClassPathResource repositoryResource) throws IOException {
        byte[] fileBytes = StreamUtils.copyToByteArray(repositoryResource.getInputStream());
        return new String(fileBytes);
    }

    @Override
    public List<T> findAll() {
        return entities;
    }
}
