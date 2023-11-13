package com.dbn.execution.common.result.ui;

import com.dbn.common.ui.form.DBNForm;
import com.dbn.execution.ExecutionResult;
import org.jetbrains.annotations.NotNull;

public interface ExecutionResultForm<E extends ExecutionResult<?>> extends DBNForm {
    @NotNull E getExecutionResult();

    void setExecutionResult(@NotNull E executionResult);
}
