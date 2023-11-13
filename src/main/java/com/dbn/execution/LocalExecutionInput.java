package com.dbn.execution;

import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.SessionId;
import com.dbn.connection.session.DatabaseSession;
import com.dbn.database.DatabaseFeature;
import com.intellij.openapi.project.Project;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;

import static com.dbn.common.options.setting.Settings.*;

@Getter
@Setter
public abstract class LocalExecutionInput extends ExecutionInput{
    private ExecutionOptions options = new ExecutionOptions();
    private SessionId targetSessionId = SessionId.MAIN;

    public LocalExecutionInput(Project project, ExecutionTarget executionTarget) {
        super(project, executionTarget);

        ConnectionHandler connection = getConnection();
        if (connection != null) {
            if (DatabaseFeature.DATABASE_LOGGING.isSupported(connection)) {
                options.set(ExecutionOption.ENABLE_LOGGING, connection.isLoggingEnabled());
            }
        }
    }

    public void setTargetSession(DatabaseSession databaseSession) {
        setTargetSessionId(databaseSession == null ? SessionId.MAIN : databaseSession.getId());
    }

    public abstract boolean hasExecutionVariables();

    public abstract boolean isSchemaSelectionAllowed();

    public abstract boolean isSessionSelectionAllowed();

    public abstract boolean isDatabaseLogProducer();

    /*********************************************************
     *                 PersistentConfiguration               *
     *********************************************************/
    @Override
    public void readConfiguration(Element element) {
        super.readConfiguration(element);
        targetSessionId = sessionIdAttribute(element, "session-id", targetSessionId);
        options.set(ExecutionOption.ENABLE_LOGGING, booleanAttribute(element, "enable-logging", true));
        options.set(ExecutionOption.COMMIT_AFTER_EXECUTION, booleanAttribute(element, "commit-after-execution", true));
    }

    @Override
    public void writeConfiguration(Element element) {
        super.writeConfiguration(element);
        element.setAttribute("session-id", targetSessionId.id());
        setBooleanAttribute(element, "enable-logging", options.is(ExecutionOption.ENABLE_LOGGING));
        setBooleanAttribute(element, "commit-after-execution", options.is(ExecutionOption.COMMIT_AFTER_EXECUTION));
    }
}
