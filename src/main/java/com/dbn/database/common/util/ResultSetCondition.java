package com.dbn.database.common.util;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetCondition {
    boolean evaluate(ResultSet source) throws SQLException;
}
