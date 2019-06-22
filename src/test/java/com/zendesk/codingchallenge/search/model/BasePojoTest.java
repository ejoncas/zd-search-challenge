package com.zendesk.codingchallenge.search.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.zendesk.codingchallenge.search.config.SearchConfig;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static com.zendesk.codingchallenge.search.utils.ResourceUtils.resourceToString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public abstract class BasePojoTest<T> {

    private Gson gson = new SearchConfig().gson();
    private JsonParser plainJsonParser = new JsonParser();

    private final Class<T> pojoClass;
    private final ClassPathResource sampleResource;

    /**
     * It receives the POJO class and also a classpath resource with a sample serialized version of the POJO.
     *
     * @param pojoClass
     * @param sampleFileResource
     */
    protected BasePojoTest(Class<T> pojoClass, ClassPathResource sampleFileResource) {
        this.pojoClass = pojoClass;
        this.sampleResource = sampleFileResource;
    }

    @Test
    public void testDeserializationAndThenSerializationIsSame() throws IOException {
        String json = resourceToString(sampleResource);

        //Plain json to a json tree
        JsonElement jsonFromFile = plainJsonParser.parse(json);

        //Plain json to our POJO
        T user = gson.fromJson(json, pojoClass);

        //POJO back to json tree -> It should match the one read directly from file
        assertThat(jsonFromFile, is(equalTo(gson.toJsonTree(user))));
    }

}