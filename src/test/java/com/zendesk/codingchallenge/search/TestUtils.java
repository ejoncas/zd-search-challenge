package com.zendesk.codingchallenge.search;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.zendesk.codingchallenge.search.config.SearchConfig;
import com.zendesk.codingchallenge.search.model.User;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang.math.RandomUtils.*;

public class TestUtils {

    @Test
    public void generateBiggerFilesForTesting() throws IOException {
        SearchConfig searchConfig = new SearchConfig();
        Gson gson = searchConfig.gson();
        User sample = searchConfig.userRepository().findAll().get(0);
        List<User> newOnes = Lists.newArrayList();

        for (int i = 0; i < 400_000; i++) {
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
        FileWriter writer = new FileWriter(tempFile);
        gson.toJson(newOnes, writer);
        writer.flush();
        writer.close();
        System.out.println("Written to " + tempFile);
    }
}
