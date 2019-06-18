package com.zendesk.codingchallenge.search.commands.output;

import com.google.common.collect.Lists;

import java.util.List;

public class TableOutput {

    private int maxWidth = 0;
    private List<KeyValue> rows = Lists.newArrayList();

    private static final class KeyValue {
        final String left;
        final String right;

        public KeyValue(String left, String right) {
            this.left = left;
            this.right = right;
        }
    }

    public void addRow(String key, String value) {
        if (key.length() > maxWidth) {
            maxWidth = key.length();
        }
        rows.add(new KeyValue(key, value));
    }

    public String render() {
        StringBuilder builder = new StringBuilder();
        String format = "%" + maxWidth + "s => %s\n";
        rows.forEach(each ->
                builder.append(String.format(format, each.left, each.right))
        );
        return builder.toString();
    }


}
