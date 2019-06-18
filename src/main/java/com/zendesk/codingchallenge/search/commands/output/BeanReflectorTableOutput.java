package com.zendesk.codingchallenge.search.commands.output;

import com.zendesk.codingchallenge.search.utils.SearchReflectionUtils;

/**
 * Formats a string as key values in a table like structure
 */
public class BeanReflectorTableOutput extends TableOutput {

    public BeanReflectorTableOutput(Object obj) {
        SearchReflectionUtils.doWithSerializedNames(obj, this::addRow);
    }

}
