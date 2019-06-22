package com.zendesk.codingchallenge.search.commands.output;

import com.zendesk.codingchallenge.search.utils.PojoJsonIntrospector;

/**
 * Formats a string as key values in a table like structure
 */
public class PojoReflectorTableOutput extends TableOutput {

    public PojoReflectorTableOutput(Object obj) {
        super(obj.getClass().getSimpleName());
        PojoJsonIntrospector.doWithSerializedNames(obj, (k, v) -> {
            this.addRow(k, String.valueOf(v));
        });
    }

}
