package com.dbn.database.common.debug;

import com.dbn.database.common.statement.CallableStatementOutput;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class ExecutionStatusInfo implements CallableStatementOutput {
    private boolean isRunning;
    @Override
    public void registerParameters(CallableStatement statement) throws SQLException {
        statement.registerOutParameter(1, Types.INTEGER);
    }

    @Override
    public void read(CallableStatement statement) throws SQLException {
        isRunning = statement.getInt(1) == 1;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
