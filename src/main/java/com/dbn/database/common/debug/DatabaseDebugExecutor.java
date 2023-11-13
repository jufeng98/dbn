package com.dbn.database.common.debug;

import com.intellij.execution.Executor;
import com.intellij.execution.ExecutorRegistry;
import com.intellij.execution.executors.DefaultDebugExecutor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DatabaseDebugExecutor extends DefaultDebugExecutor{

    public static final String EXECUTOR_ID = "Database Debug";

    @NotNull
    @Override
    public String getId() {
        return EXECUTOR_ID;
    }

    @Override
    public String getContextActionId() {
        return "DatabaseDebug";
    }


    @NotNull
    @Override
    public Icon getIcon() {
        return super.getIcon();
    }

    public static Executor getDebugExecutorInstance() {
        return ExecutorRegistry.getInstance().getExecutorById(EXECUTOR_ID);
    }
}
