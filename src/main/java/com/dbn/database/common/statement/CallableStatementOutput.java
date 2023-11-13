package com.dbn.database.common.statement;

import java.sql.CallableStatement;
import java.sql.SQLException;

public interface CallableStatementOutput {
    void registerParameters(CallableStatement statement) throws SQLException;
    void read(CallableStatement statement) throws SQLException;
}
