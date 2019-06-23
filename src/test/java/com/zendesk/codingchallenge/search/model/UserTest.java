package com.zendesk.codingchallenge.search.model;

import com.zendesk.codingchallenge.search.TestUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import static org.junit.Assert.*;

public class UserTest extends BasePojoTest<User> {

    public UserTest() {
        super(User.class, new ClassPathResource("single_user.json"));
    }

    @Test
    public void testPojo() {
        //Given
        User user1 = TestUtils.sampleUser();
        user1.setId(2);
        User user2 = TestUtils.sampleUser();
        user2.setId(2);
        User user3 = TestUtils.sampleUser();
        user3.setId(3);

        //Then
        assertEquals(user1, user2);
        assertNotEquals(user2, user3);
    }
}