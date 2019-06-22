package com.zendesk.codingchallenge.search.commands.output;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class TableOutputTest {

    @Test
    public void testTableOutputSingle() {
        TableOutput output = new TableOutput("Results");

        output.addRow("a", "b");
        output.addRow("cccccccc", "ddddddd");
        output.addRow("verylongstring should push columns to the right", "value");

        //Just in case someone uses Windows...
        String separator = TableOutput.NEW_LINE;
        assertThat(output.render(), is(equalTo(
                "Results" + separator +
                        "                                              a => b" + separator +
                        "                                       cccccccc => ddddddd" + separator +
                        "verylongstring should push columns to the right => value" + separator
        )));
    }

    @Test
    public void testTableOutputMultipleTables() {
        TableOutput output = new TableOutput("Results");

        output.addRow("a", "b");
        output.addRow("aaaaa", "bbbbbb");

        output.addTableBreak("2nd Table");

        output.addRow("ccccccccccccc", "ddddddddddddddd");
        output.addRow("ffff", "ggg");

        output.addTableBreak("3rd Table");

        output.addRow("11", "22");
        output.addRow("x", "z");

        //Just in case someone uses Windows...
        String separator = TableOutput.NEW_LINE;
        assertThat(output.render(), is(equalTo(
                "Results" + separator +
                        "            a => b" + separator +
                        "        aaaaa => bbbbbb" + separator +
                        separator +
                        "--------------------------" + separator +
                        "2nd Table" + separator +
                        "ccccccccccccc => ddddddddddddddd" + separator +
                        "         ffff => ggg" + separator +
                        separator +
                        "--------------------------" + separator +
                        "3rd Table" + separator +
                        "           11 => 22" + separator +
                        "            x => z" + separator
        )));
    }

}