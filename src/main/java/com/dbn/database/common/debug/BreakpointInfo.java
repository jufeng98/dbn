package com.dbn.database.common.debug;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class BreakpointInfo extends BasicOperationInfo {
    private Integer breakpointId;

    @Override
    public void registerParameters(CallableStatement statement) throws SQLException {
        statement.registerOutParameter(1, Types.NUMERIC);
        statement.registerOutParameter(2, Types.VARCHAR);
    }

    @Override
    public void read(CallableStatement statement) throws SQLException {
        Object object = statement.getObject(1);
        if (object instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) object;
            breakpointId = bigDecimal.intValue();
        }

        error = statement.getString(2);
    }
}
