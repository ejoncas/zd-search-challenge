package com.zendesk.codingchallenge.search.model;

import org.springframework.core.io.ClassPathResource;

public class OrganisationTest extends BasePojoTest<Organisation> {

    public OrganisationTest() {
        super(Organisation.class, new ClassPathResource("single_organisation.json"));
    }

}