package com.dbn.execution;


import com.dbn.common.property.PropertyHolderBase;

public class ExecutionOptions extends PropertyHolderBase.IntStore<ExecutionOption> {

    public ExecutionOptions(ExecutionOption... properties) {
        super(properties);
    }

    public static ExecutionOptions clone(ExecutionOptions source) {
        ExecutionOptions options = new ExecutionOptions();
        options.inherit(source);
        return options;
    }

    @Override
    protected ExecutionOption[] properties() {
        return ExecutionOption.VALUES;
    }
}
