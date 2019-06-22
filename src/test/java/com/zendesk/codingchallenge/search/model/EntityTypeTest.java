package com.zendesk.codingchallenge.search.model;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class EntityTypeTest {

    @Test
    public void testEntityType() {
        for (EntityType type : EntityType.values()) {
            assertThat(type, is(equalTo(EntityType.fromName(type.getName()))));
        }
    }

}