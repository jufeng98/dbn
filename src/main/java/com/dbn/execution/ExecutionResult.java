package com.dbn.execution;

import com.dbn.common.dispose.StatefulDisposable;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionId;
import com.dbn.execution.common.result.ui.ExecutionResultForm;
import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public interface ExecutionResult<F extends ExecutionResultForm> extends StatefulDisposable, DataProvider {

    @Nullable
    F createForm();

    @Nullable
    default F getForm() {
        Project project = getProject();
        ExecutionManager executionManager = ExecutionManager.getInstance(project);
        return executionManager.getExecutionResultForm(this);
    }

    @NotNull
    String getName();

    default void setName(@NotNull String name, boolean sticky) {}

    Icon getIcon();

    @NotNull
    Project getProject();

    ConnectionId getConnectionId();

    @NotNull
    ConnectionHandler getConnection();

    PsiFile createPreviewFile();

    ExecutionResult<F> getPrevious();

    void setPrevious(ExecutionResult<F> previous);
}
