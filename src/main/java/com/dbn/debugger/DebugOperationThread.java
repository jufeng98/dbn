package com.dbn.debugger;

import com.dbn.common.notification.NotificationGroup;
import com.dbn.common.notification.NotificationSupport;
import com.intellij.openapi.project.Project;

import java.sql.SQLException;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

public abstract class DebugOperationThread extends Thread implements NotificationSupport {
    private Project project;
    private String operationName;

    protected DebugOperationThread(Project project, String operationName) {
        super("DBN Debug Operation (" + operationName + ')');
        this.project = project;
        this.operationName = operationName;
    }

    @Override
    public Project getProject() {
        return project;
    }

    @Override
    public final void run() {
        try {
            executeOperation();
        } catch (SQLException e) {
            conditionallyLog(e);
            sendErrorNotification(
                    NotificationGroup.DEBUGGER,
                    "Error performing operation ({0}): {1}", operationName, e);
        }
    }

    public abstract void executeOperation() throws SQLException;
}
