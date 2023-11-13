package com.dci.intellij.dbn.execution.logging;

import com.dci.intellij.dbn.common.action.DataKeys;
import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.util.Commons;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionId;
import com.dci.intellij.dbn.database.interfaces.DatabaseCompatibilityInterface;
import com.dci.intellij.dbn.execution.ExecutionResultBase;
import com.dci.intellij.dbn.execution.logging.ui.DatabaseLoggingResultConsole;
import com.dci.intellij.dbn.execution.logging.ui.DatabaseLoggingResultForm;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.dci.intellij.dbn.common.dispose.Checks.isNotValid;

public class DatabaseLoggingResult extends ExecutionResultBase<DatabaseLoggingResultForm> {

    private LogOutputContext context;

    public DatabaseLoggingResult(@NotNull LogOutputContext context) {
        this.context = context;
    }

    @Nullable
    @Override
    public DatabaseLoggingResultForm createForm() {
        return new DatabaseLoggingResultForm(this);
    }

    @NotNull
    public LogOutputContext getContext() {
        return Failsafe.nn(context);
    }

    @Override
    @NotNull
    public String getName() {
        ConnectionHandler connection = getConnection();
        VirtualFile sourceFile = context.getSourceFile();
        if (sourceFile == null) {
            DatabaseCompatibilityInterface compatibility = connection.getCompatibilityInterface();
            String databaseLogName = compatibility.getDatabaseLogName();

            return connection.getName() + " - " + Commons.nvl(databaseLogName, "Log Output");
        } else {
            return connection.getName() + " - " + sourceFile.getName();
        }
    }

    public boolean matches(LogOutputContext context) {
        return this.context == context || this.context.matches(context);
    }

    @Override
    public Icon getIcon() {
        return Icons.EXEC_LOG_OUTPUT_CONSOLE;
    }

    @NotNull
    @Override
    public Project getProject() {
        return getConnection().getProject();
    }

    @Override
    public ConnectionId getConnectionId() {
        return context.getConnectionId();
    }

    @Nullable
    public VirtualFile getSourceFile() {
        return context.getSourceFile();
    }

    @NotNull
    @Override
    public ConnectionHandler getConnection() {
        return context.getConnection();
    }

    @Override
    public PsiFile createPreviewFile() {
        return null;
    }

    public void write(LogOutputContext context, LogOutput output) {
        this.context = context;
        DatabaseLoggingResultForm resultForm = getForm();
        if (isNotValid(resultForm)) return;

        DatabaseLoggingResultConsole console = resultForm.getConsole();
        if (output.isClearBuffer()) {
            console.clear();
        }
        if (output.isScrollToEnd()) {
            ConsoleView consoleView = console.getConsole();
            if (consoleView instanceof ConsoleViewImpl) {
                ConsoleViewImpl consoleViewImpl = (ConsoleViewImpl) consoleView;
                consoleViewImpl.requestScrollingToEnd();
            }
        }
        console.writeToConsole(context, output);
    }

    /********************************************************
     *                    Data Provider                     *
     ********************************************************/
    @Nullable
    @Override
    public Object getData(@NotNull String dataId) {
        if (DataKeys.DATABASE_LOG_OUTPUT.is(dataId)) {
            return DatabaseLoggingResult.this;
        }
        return null;
    }


}
