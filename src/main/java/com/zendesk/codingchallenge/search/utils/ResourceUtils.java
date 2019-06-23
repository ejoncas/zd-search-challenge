package com.zendesk.codingchallenge.search.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;

public final class ResourceUtils {

    private ResourceUtils() {
        throw new RuntimeException("No instances allowed!");
    }

    public static String resourceToString(String resourceAsString) throws IOException {
        return resourceToString(new ClassPathResource(resourceAsString));
    }

    public static String resourceToString(ClassPathResource repositoryResource) throws IOException {
        try (InputStream inputStream = repositoryResource.getInputStream()) {
            byte[] fileBytes = StreamUtils.copyToByteArray(inputStream);
            return new String(fileBytes);
        }
    }
}
