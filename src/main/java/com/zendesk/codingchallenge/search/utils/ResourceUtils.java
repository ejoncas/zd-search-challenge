package com.zendesk.codingchallenge.search.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;

public class ResourceUtils {


    public static String resourceToString(ClassPathResource repositoryResource) throws IOException {
        try (InputStream inputStream = repositoryResource.getInputStream()) {
            byte[] fileBytes = StreamUtils.copyToByteArray(inputStream);
            return new String(fileBytes);
        }
    }
}
