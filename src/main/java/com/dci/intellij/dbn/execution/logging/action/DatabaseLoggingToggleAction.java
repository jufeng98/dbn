package com.dci.intellij.dbn.execution.logging.action;

import com.dci.intellij.dbn.common.action.Lookups;
import com.dci.intellij.dbn.common.action.ToggleAction;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.mapping.FileConnectionContextManager;
import com.dci.intellij.dbn.database.DatabaseFeature;
import com.dci.intellij.dbn.database.interfaces.DatabaseCompatibilityInterface;
import com.dci.intellij.dbn.debugger.DatabaseDebuggerManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dci.intellij.dbn.common.dispose.Checks.isValid;

public class DatabaseLoggingToggleAction extends ToggleAction implements DumbAware {

    public DatabaseLoggingToggleAction() {
        super("Enable / Disable Database Logging", null, Icons.EXEC_LOG_OUTPUT_CONSOLE);
    }

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        ConnectionHandler activeConnection = getConnection(e);
        return activeConnection != null && activeConnection.isLoggingEnabled();
    }

    @Nullable
    private static ConnectionHandler getConnection(AnActionEvent e) {
        Project project = Lookups.getProject(e);
        VirtualFile virtualFile = Lookups.getVirtualFile(e);
        if (project != null && virtualFile != null) {
            FileConnectionContextManager contextManager = FileConnectionContextManager.getInstance(project);
            ConnectionHandler connection = contextManager.getConnection(virtualFile);
            if (isValid(connection) && !connection.isVirtual()) {
                return connection ;
            }

        }
        return null;
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean selected) {
        ConnectionHandler connection = getConnection(e);
        if (connection != null) connection.setLoggingEnabled(selected);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        ConnectionHandler connection = getConnection(e);
        Presentation presentation = e.getPresentation();

        boolean visible = false;
        String name = "Database Logging";
        if (connection != null) {
            boolean supportsLogging = DatabaseFeature.DATABASE_LOGGING.isSupported(connection);
            if (supportsLogging && isVisible(e)) {
                visible = true;
                DatabaseCompatibilityInterface compatibility = connection.getCompatibilityInterface();
                String databaseLogName = compatibility.getDatabaseLogName();
                if (Strings.isNotEmpty(databaseLogName)) {
                    name = name + " (" + databaseLogName + ")";
                }
            }
        }
        presentation.setText(name);
        presentation.setVisible(visible);
    }

    public static boolean isVisible(AnActionEvent e) {
        VirtualFile virtualFile = Lookups.getVirtualFile(e);
        return !DatabaseDebuggerManager.isDebugConsole(virtualFile);
    }
}