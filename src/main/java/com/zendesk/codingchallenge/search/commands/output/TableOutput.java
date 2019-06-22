package com.zendesk.codingchallenge.search.commands.output;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * This class outputs key value pairs using the following format:
 * <p>
 * <p>
 * <code>
 * <p>
 * key1 => value1
 * key2 => value2
 * verylongkey => verylongvalue
 * </code>
 */
public class TableOutput {

    protected static final String NEW_LINE = System.lineSeparator();
    private int leftWidth = 0;
    private Map<String, String> currentTable;
    private List<TableWithName> allTables;

    private static final class TableWithName {
        private String name;
        private Map<String, String> table;

        public TableWithName(String name, Map<String, String> table) {
            this.name = name;
            this.table = table;
        }
    }

    public TableOutput(String headerName) {
        //Use a TreeMap to keep them sorted on natural order (lexicographically)
        this.allTables = Lists.newArrayList();
        this.addTableBreak(headerName);
    }

    /**
     * Adds a key and value to be rendered in the output
     *
     * @param key   the key
     * @param value the value
     */
    public void addRow(String key, String value) {
        if (key.length() > leftWidth) {
            leftWidth = key.length();
        }
        currentTable.put(key, value);
    }

    public void addTableBreak(String tableName) {
        currentTable = Maps.newTreeMap();
        allTables.add(new TableWithName(tableName, currentTable));
    }

    /**
     * Renders existing tables as String, leaving a separator between them.
     *
     * @return The formatted tables as a String
     */
    public String render() {
        StringBuilder builder = new StringBuilder();
        String format = "%" + leftWidth + "s => %s" + NEW_LINE;

        int totalTables = allTables.size();
        for (int i = 0; i < totalTables; i++) {
            TableWithName tableWithName = allTables.get(i);
            builder.append(tableWithName.name);
            builder.append(NEW_LINE);
            tableWithName.table.forEach((k, v) ->
                    builder.append(String.format(format, k, v))
            );
            if (i < totalTables - 1) {
                builder.append(NEW_LINE);
                builder.append(StringUtils.repeat("-", leftWidth * 2));
                builder.append(NEW_LINE);
            }
        }
        return builder.toString();
    }


}
