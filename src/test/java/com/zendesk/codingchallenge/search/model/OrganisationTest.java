package com.zendesk.codingchallenge.search.model;

import com.zendesk.codingchallenge.search.TestUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class OrganisationTest extends BasePojoTest<Organisation> {

    public OrganisationTest() {
        super(Organisation.class, new ClassPathResource("single_organisation.json"));
    }

    @Test
    public void testPojo() {
        //Given
        Organisation organisation1 = TestUtils.sampleOrganisation();
        organisation1.setId(6);
        Organisation organisation2 = TestUtils.sampleOrganisation();
        organisation2.setId(6);
        Organisation organisation3 = TestUtils.sampleOrganisation();
        organisation3.setId(3);

        //Then
        assertEquals(organisation1, organisation2);
        assertNotEquals(organisation2, organisation3);
    }

}