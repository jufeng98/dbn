package com.dbn.database.common.debug;

import com.dbn.database.common.statement.CallableStatementOutput;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

@Getter
@Setter
@EqualsAndHashCode
public class BasicOperationInfo implements CallableStatementOutput {
    protected String error;

    @Override
    public void registerParameters(CallableStatement statement) throws SQLException {
        statement.registerOutParameter(1, Types.VARCHAR);
    }

    @Override
    public void read(CallableStatement statement) throws SQLException {
        error = statement.getString(1);
    }
}
