package com.zendesk.codingchallenge.search.model;

import org.springframework.core.io.ClassPathResource;

public class UserTest extends BasePojoTest<User> {

    public UserTest() {
        super(User.class, new ClassPathResource("single_user.json"));
    }
}