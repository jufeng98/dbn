package com.dbn.database.common;

import com.dbn.database.common.execution.MethodExecutionProcessor;
import com.dbn.database.common.execution.SimpleFunctionExecutionProcessor;
import com.dbn.database.common.execution.SimpleProcedureExecutionProcessor;
import com.dbn.database.interfaces.DatabaseExecutionInterface;
import com.dbn.object.DBFunction;
import com.dbn.object.DBMethod;
import com.dbn.object.DBProcedure;

public abstract class DatabaseExecutionInterfaceImpl implements DatabaseExecutionInterface {

    public MethodExecutionProcessor createSimpleMethodExecutionProcessor(DBMethod method) {
        if (method instanceof DBFunction) {
            DBFunction function = (DBFunction) method;
            return new SimpleFunctionExecutionProcessor(function);
        }
        if (method instanceof DBProcedure) {
            DBProcedure procedure = (DBProcedure) method;
            return new SimpleProcedureExecutionProcessor(procedure);

        }
        return null;
    }

}
