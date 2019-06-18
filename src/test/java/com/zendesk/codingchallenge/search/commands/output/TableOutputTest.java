package com.zendesk.codingchallenge.search.commands.output;

import org.junit.Test;

public class TableOutputTest {


    @Test
    public void testTableOutput() {
        TableOutput output = new TableOutput();

        output.addRow("a", "b");
        output.addRow("cccccccc", "ddddddd");
        output.addRow("verylongstring should push columns to the right", "value");

        //FIXME
        System.out.println(output.render());
    }

}