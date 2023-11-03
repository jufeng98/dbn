package com.dci.intellij.dbn.database.common.logging;

import com.dci.intellij.dbn.data.value.ClobValue;
import com.dci.intellij.dbn.database.common.statement.CallableStatementOutput;
import lombok.Getter;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

@Getter
public class ExecutionLogOutput implements CallableStatementOutput{
    private String log;

    @Override
    public void registerParameters(CallableStatement statement) throws SQLException {
        statement.registerOutParameter(1, Types.CLOB);
    }

    @Override
    public void read(CallableStatement statement) throws SQLException {
        log = new ClobValue(statement, 1).read();
    }

}
