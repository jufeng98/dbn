package com.dbn.execution.statement.processor;

import com.dbn.common.dispose.StatefulDisposable;
import com.dbn.common.navigation.NavigationInstructions;
import com.dbn.common.ui.Presentable;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.SchemaId;
import com.dbn.connection.context.DatabaseContextBase;
import com.dbn.connection.jdbc.DBNConnection;
import com.dbn.connection.session.DatabaseSession;
import com.dbn.editor.EditorProviderId;
import com.dbn.execution.statement.variables.StatementExecutionVariablesBundle;
import com.dbn.execution.statement.StatementExecutionContext;
import com.dbn.execution.statement.StatementExecutionInput;
import com.dbn.execution.statement.result.StatementExecutionResult;
import com.dbn.language.common.DBLanguagePsiFile;
import com.dbn.language.common.psi.ExecutablePsiElement;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;

public interface StatementExecutionProcessor extends DatabaseContextBase, StatefulDisposable, Presentable {

    boolean isDirty();

    @Override
    @Nullable
    ConnectionHandler getConnection();

    @NotNull
    ConnectionHandler getTargetConnection();

    @Nullable
    SchemaId getTargetSchema();

    @Nullable
    DatabaseSession getTargetSession();

    @NotNull
    Project getProject();

    @Nullable
    DBLanguagePsiFile getPsiFile();

    @Nullable
    VirtualFile getVirtualFile();

    @NotNull
    String getResultName();

    void setResultName(String resultName, boolean sticky);

    String getStatementName();

    void navigateToResult();

    void navigateToEditor(NavigationInstructions instructions);

    void execute() throws SQLException;

    void execute(@Nullable DBNConnection connection, boolean debug) throws SQLException;

    void postExecute();

    void cancelExecution();

    @Nullable
    StatementExecutionVariablesBundle getExecutionVariables();

    void bind(ExecutablePsiElement executablePsiElement);

    void unbind();

    boolean isBound();

    @Nullable
    FileEditor getFileEditor();

    @Nullable
    EditorProviderId getEditorProviderId();

    @Nullable
    ExecutablePsiElement getCachedExecutable();

    StatementExecutionInput getExecutionInput();

    @Nullable
    StatementExecutionResult getExecutionResult();

    void initExecutionInput(boolean bulkExecution);

    boolean isQuery();

    int getExecutableLineNumber();

    StatementExecutionContext getExecutionContext();

    StatementExecutionContext initExecutionContext();
}
