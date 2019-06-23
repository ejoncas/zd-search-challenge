package com.zendesk.codingchallenge.search.utils;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ResourceUtilsTest {

    @Test
    public void testSimpleResourceToString() throws IOException {
        assertThat(ResourceUtils.resourceToString(new ClassPathResource("test_file.txt")), is("this is just for testing"));
    }

}