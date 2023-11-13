package com.dbn.database.common.execution;

import com.dbn.execution.method.MethodExecutionInput;
import com.dbn.object.DBFunction;

public class SimpleFunctionExecutionProcessor extends MethodExecutionProcessorImpl {
    public SimpleFunctionExecutionProcessor(DBFunction function) {
        super(function);
    }

    @Override
    public String buildExecutionCommand(MethodExecutionInput executionInput) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{? = call ");
        buffer.append(getMethod().getQualifiedName());
        buffer.append("(");
        for (int i=1; i< getArgumentsCount(); i++) {
            if (i>1) buffer.append(",");
            buffer.append("?");
        }
        buffer.append(")}");
        return buffer.toString();
    }
}