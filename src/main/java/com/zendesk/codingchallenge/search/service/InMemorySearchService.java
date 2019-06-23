package com.zendesk.codingchallenge.search.service;

import com.google.common.base.Stopwatch;
import com.google.common.collect.*;
import com.zendesk.codingchallenge.search.exception.SearchCommandFailedException;
import com.zendesk.codingchallenge.search.model.BaseEntity;
import com.zendesk.codingchallenge.search.repository.EntityRepository;
import com.zendesk.codingchallenge.search.utils.PojoJsonIntrospectorUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.joining;

/**
 * An in-memory search service.
 * <p>
 * TODO: Fix documentation
 * This class holds a reverse index where every word gets mapped to the entity.
 *
 * @param <T>
 */
public class InMemorySearchService<ID, T extends BaseEntity<ID>> implements SearchService<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemorySearchService.class);
    private static final String WORD_SEPARATOR = " ";
    public static final String NULL_KEYWORD = "<null>";
    private EntityRepository<ID, T> repository;

    /**
     * Double entry table where:
     * <p>
     * <ul>
     * <li>First Key: Field Name</li>
     * <li>Second Key: Word to search for</li>
     * <li>Value: The List of matched entities for that field and word</li>
     * </ul>
     */
    private Table<String, String, List<T>> indexByFieldAndWords = HashBasedTable.create();
    private boolean indexed = false;

    public InMemorySearchService(EntityRepository<ID, T> repository) {
        this.repository = repository;
    }

    @Override
    @PostConstruct
    public void index() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Collection<T> entities = repository.findAll();
        for (T entity : entities) {
            PojoJsonIntrospectorUtils.doWithSerializedNames(entity, (name, value) -> {
                String[] words = valueToString(value).split(WORD_SEPARATOR);
                for (String word : words) {
                    String sanitizedWord = sanitizeValue(word);
                    List<T> matchedEntities = indexByFieldAndWords.get(name, sanitizedWord);
                    if (matchedEntities == null) {
                        matchedEntities = Lists.newArrayList();
                        indexByFieldAndWords.put(name, sanitizedWord, matchedEntities);
                    }
                    matchedEntities.add(entity);
                }
            });
        }
        indexed = true;
        LOGGER.info("Indexing {} entities took {}ms", entities.size(), stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
    }


    /**
     * Sanitize value before splitting into words.
     *
     * @param value the value, un-sanitized
     * @return the sanitizedValue
     */
    private String sanitizeValue(String value) {
        return StringUtils.removeEnd(value.trim(), ".");
    }

    /**
     * Returns the string to index. This is particularly useful for collections type where the default toString
     * adds square brackets at the start and the end.
     *
     * @param value the value of the field to index
     * @return the String to be indexed. Word separated.
     */
    private String valueToString(Object value) {
        if (value == null) {
            return NULL_KEYWORD;
        } else if (Collection.class.isAssignableFrom(value.getClass())) {
            Collection<?> collectionValue = (Collection<?>) value;
            return collectionValue.stream().map(String::valueOf).collect(joining(WORD_SEPARATOR));
        } else {
            return String.valueOf(value);
        }
    }

    @Override
    public Set<String> getIndexedFields() {
        validateCurrentState();
        return ImmutableSortedSet.copyOf(indexByFieldAndWords.rowKeySet());
    }

    private void validateCurrentState() {
        if (!indexed) {
            throw new SearchCommandFailedException("Search service has not been initialized. Index results before searching!");
        }
    }

    @Override
    public List<T> search(String field, String searchTerm) {
        validateCurrentState();
        List<T> results = indexByFieldAndWords.get(field, searchTerm);
        return results == null ? ImmutableList.of() : Collections.unmodifiableList(results);
    }
}
