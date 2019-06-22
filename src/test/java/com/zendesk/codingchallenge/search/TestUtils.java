package com.zendesk.codingchallenge.search;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.zendesk.codingchallenge.search.config.SearchConfig;
import com.zendesk.codingchallenge.search.model.User;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang.math.RandomUtils.nextBoolean;
import static org.apache.commons.lang.math.RandomUtils.nextInt;

public class TestUtils {

    public static final int NUMBER_OF_USERS_IN_FILE = 400_000;

    /**
     * This file has been used to create really big files for testing.
     * I'm not including any file in the  repo as they  were huge.
     * <p>
     * This test is ignored to not generate huge files when you run the build.
     * <p>
     * If needed, it can be un-ignored and it will generate a JSON in your temp folder
     *
     * @throws IOException
     */
    @Test
    @Ignore
    public void generateBiggerFilesForTesting() throws IOException {
        SearchConfig searchConfig = new SearchConfig();
        Gson gson = searchConfig.gson();
        //Get a sample
        User sample = searchConfig.userRepository().findAll().stream().findAny()
                .orElseThrow(() -> new IllegalStateException("No entities found"));
        List<User> newOnes = Lists.newArrayList();

        for (int i = 0; i < NUMBER_OF_USERS_IN_FILE; i++) {
            //Add some randomness to make sure we get lots of new words to be indexed.
            User deepCopy = gson.fromJson(gson.toJson(sample), User.class);
            deepCopy.setName(RandomStringUtils.randomAlphanumeric(10));
            deepCopy.setTags(ImmutableSortedSet.of(
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(5),
                    RandomStringUtils.randomAlphabetic(20)
            ));
            deepCopy.setAlias(RandomStringUtils.randomAlphabetic(200));
            deepCopy.setShared(nextBoolean());
            deepCopy.setActive(nextBoolean());
            deepCopy.setId(nextInt());
            deepCopy.setExternalId(UUID.randomUUID().toString());
            deepCopy.setEmail(RandomStringUtils.randomAlphanumeric(50));
            newOnes.add(deepCopy);
        }
        File tempFile = File.createTempFile("user", ".json");
        try (FileWriter writer = new FileWriter(tempFile)) {
            gson.toJson(newOnes, writer);
        }
        System.out.println("Written to " + tempFile);
    }
}
