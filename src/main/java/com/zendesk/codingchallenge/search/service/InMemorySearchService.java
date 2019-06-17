package com.zendesk.codingchallenge.search.service;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.google.gson.annotations.SerializedName;
import com.zendesk.codingchallenge.search.model.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * An in-memory search service.
 * <p>
 * TODO: Fix documentation
 * This class holds a reverse index where every word gets mapped to the entity
 *
 * @param <T>
 */
public class InMemorySearchService<T extends BaseEntity> implements SearchService<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemorySearchService.class);
    private static final String WORD_SEPARATOR = " ";

    private final List<T> entities;
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

    public InMemorySearchService(List<T> entities) {
        this.entities = entities;
    }

    @Override
    @PostConstruct
    public void index() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (T entity : entities) {
            ReflectionUtils.doWithFields(entity.getClass(), f -> {
                SerializedName serializedName = f.getAnnotation(SerializedName.class);
                String indexFieldName = serializedName == null ? f.getName() : serializedName.value();
                ReflectionUtils.makeAccessible(f);
                String fieldValue = String.valueOf(f.get(entity));
                String[] words = Strings.nullToEmpty(fieldValue).split(WORD_SEPARATOR);
                for (String word : words) {
                    List<T> matchedEntities = indexByFieldAndWords.get(indexFieldName, word);
                    if (matchedEntities == null) {
                        matchedEntities = Lists.newArrayList();
                        indexByFieldAndWords.put(indexFieldName, word, matchedEntities);
                    }
                    matchedEntities.add(entity);
                }
            });
        }
        indexed = true;
        LOGGER.info("Indexing {} entities took {}ms", entities.size(), stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
    }

    @Override
    public Set<String> getIndexedFields() {
        validateCurrentState();
        return ImmutableSortedSet.copyOf(indexByFieldAndWords.rowKeySet());
    }

    private void validateCurrentState() {
        if (!indexed) {
            throw new IllegalStateException("Search service has not been initialized. Index results before searching!");
        }
    }

    @Override
    public List<T> search(String field, String searchTerm) {
        validateCurrentState();
        return indexByFieldAndWords.get(field, searchTerm);
    }
}
