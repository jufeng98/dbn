package com.dbn.database.sqlite.adapter.rs;

import com.dbn.common.util.Strings;
import com.dbn.database.common.util.WrappedResultSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqliteTriggersResultSet extends WrappedResultSet {
    private static final Pattern TRIGGER_EVENT_REGEX = Pattern.compile("(before|after|instead\\s+of)\\s+(delete|insert|update)", Pattern.CASE_INSENSITIVE);

    public SqliteTriggersResultSet(ResultSet resultSet) {
        super(resultSet);
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        boolean isType = Objects.equals(columnLabel, "TRIGGER_TYPE");
        boolean isEvent = Objects.equals(columnLabel, "TRIGGERING_EVENT");
        if (isType || isEvent) {
            String sourceCode = inner.getString("SOURCE_CODE");

            Matcher m = TRIGGER_EVENT_REGEX.matcher(sourceCode);
            if (m.find()) {
                int start = m.start();
                int end = m.end();
                String definition = sourceCode.substring(start, end);
                if (isType) {
                    return
                        Strings.containsIgnoreCase(definition, "before") ? "BEFORE" :
                        Strings.containsIgnoreCase(definition, "after") ? "AFTER" :
                        Strings.containsIgnoreCase(definition, "instead") ? "INSTEAD OF" : "";
                }
                if (isEvent) {
                    return
                        Strings.containsIgnoreCase(definition, "delete") ? "DELETE" :
                        Strings.containsIgnoreCase(definition, "insert") ? "INSERT" :
                        Strings.containsIgnoreCase(definition, "update") ? "UPDATE" : "";

                }
            }
        }
        return inner.getString(columnLabel);
    }
}
