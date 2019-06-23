package com.zendesk.codingchallenge.search.utils;

import com.google.common.collect.Maps;
import com.google.gson.annotations.SerializedName;
import com.zendesk.codingchallenge.search.exception.SearchCommandFailedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertThat;

public class PojoJsonIntrospectorUtilsTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testSearchReflectionUtils() {
        //Given
        ATestClassToIntrospect testObject = new ATestClassToIntrospect();
        testObject.setRegularPrivateField("regularPrivateFieldValue");
        testObject.setRegularPrivateFieldWithAnnotation("regularPrivateFieldWithAnnotationValue");


        //When
        Map<String, Object> fieldsValues = Maps.newLinkedHashMap();
        PojoJsonIntrospectorUtils.doWithSerializedNames(testObject, fieldsValues::put);

        //Then
        //it should have only 2 entries as 'noGetterField' has no getter. Hence it is not visible
        assertThat(fieldsValues.size(), is(2));
        assertThat(fieldsValues, hasEntry("regularPrivateField", "regularPrivateFieldValue"));
        assertThat(fieldsValues, hasEntry("regular_private_field_with_annotation", "regularPrivateFieldWithAnnotationValue"));
    }

    @Test
    public void testSearchShouldThrowExceptionWithInvalidPojo() {
        //Given
        AClassWithInvalidConventions invalidPojo = new AClassWithInvalidConventions();

        //When
        expectedException.expect(SearchCommandFailedException.class);
        PojoJsonIntrospectorUtils.doWithSerializedNames(invalidPojo, (k, v) -> {
        });
    }

    @Test
    public void testSearchShouldThrowExceptionWhenInvocationException() {
        //Given
        AClassWithInvocationExxception invalidPojo = new AClassWithInvocationExxception();

        //When
        expectedException.expect(SearchCommandFailedException.class);
        expectedException.expectCause(isA(InvocationTargetException.class));
        expectedException.expectMessage("Failed to introspect pojo");
        PojoJsonIntrospectorUtils.doWithSerializedNames(invalidPojo, (k, v) -> {
        });
    }

    public static final class ATestClassToIntrospect {

        private String regularPrivateField;
        @SerializedName("regular_private_field_with_annotation")
        private String regularPrivateFieldWithAnnotation;
        private String noGetterField;

        public String getRegularPrivateField() {
            return regularPrivateField;
        }

        public void setRegularPrivateField(String regularPrivateField) {
            this.regularPrivateField = regularPrivateField;
        }

        public String getRegularPrivateFieldWithAnnotation() {
            return regularPrivateFieldWithAnnotation;
        }

        public void setRegularPrivateFieldWithAnnotation(String regularPrivateFieldWithAnnotation) {
            this.regularPrivateFieldWithAnnotation = regularPrivateFieldWithAnnotation;
        }
    }

    public static final class AClassWithInvocationExxception {
        private String aPrivateField;

        public String getaPrivateField() {
            throw new RuntimeException("Boom!");
        }
    }

    private static final class AClassWithInvalidConventions {

        public String getAFieldThatDoesNotExist() {
            return "Should not get to this point";
        }

    }

}