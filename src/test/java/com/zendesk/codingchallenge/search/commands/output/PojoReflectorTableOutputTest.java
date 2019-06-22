package com.zendesk.codingchallenge.search.commands.output;

import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class PojoReflectorTableOutputTest {

    @Test
    public void testPojoTableOutput() {
        //Given
        ATestOutputClass testClass = new ATestOutputClass();

        //When
        PojoReflectorTableOutput tableOutput = new PojoReflectorTableOutput(testClass);

        //Then
        String separator = System.lineSeparator();
        assertThat(tableOutput.render(), is(equalTo(
                "ATestOutputClass" + separator +
                        "                                 shortField => shortFieldValue" + separator +
                        "                                  uuidField => 55a65d08-109e-47e4-a827-a76cc3adeb19" + separator +
                        "veryLongFieldThatWillPushTheTableToTheRight => 1234" + separator
        )));
    }

    public static final class ATestOutputClass {
        private String shortField = "shortFieldValue";
        private Integer veryLongFieldThatWillPushTheTableToTheRight = 1234;
        private UUID uuidField = UUID.fromString("55a65d08-109e-47e4-a827-a76cc3adeb19");

        public String getShortField() {
            return shortField;
        }

        public void setShortField(String shortField) {
            this.shortField = shortField;
        }

        public Integer getVeryLongFieldThatWillPushTheTableToTheRight() {
            return veryLongFieldThatWillPushTheTableToTheRight;
        }

        public void setVeryLongFieldThatWillPushTheTableToTheRight(Integer veryLongFieldThatWillPushTheTableToTheRight) {
            this.veryLongFieldThatWillPushTheTableToTheRight = veryLongFieldThatWillPushTheTableToTheRight;
        }

        public UUID getUuidField() {
            return uuidField;
        }

        public void setUuidField(UUID uuidField) {
            this.uuidField = uuidField;
        }
    }


}