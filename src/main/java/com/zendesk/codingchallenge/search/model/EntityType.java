package com.zendesk.codingchallenge.search.model;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum EntityType {

    TICKET("ticket"),
    ORGANISATION("organisation"),
    USER("user");

    private static final Map<String, EntityType> LOOKUP = Arrays.stream(values())
            .collect(Collectors.toMap(EntityType::getName, Function.identity()));

    private final String name;

    EntityType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static final EntityType fromName(String name) {
        EntityType entityType = LOOKUP.get(name);
        if (entityType == null) {
            throw new IllegalArgumentException("Name " + name + " is not a valid entity type. Accepted values are: " + LOOKUP.keySet());
        }
        return entityType;
    }


}
