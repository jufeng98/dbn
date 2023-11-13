package com.dbn.execution.statement.variables;

import com.dbn.data.type.GenericDataType;

public abstract class VariableValueProvider {
    public abstract String getValue();
    public abstract GenericDataType getDataType();
}
