package com.dbn.execution.explain;

import com.dbn.common.component.ProjectComponentBase;
import com.dbn.common.routine.Consumer;
import com.dbn.common.thread.Progress;
import com.dbn.connection.ConnectionAction;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.Resources;
import com.dbn.connection.SchemaId;
import com.dbn.connection.mapping.FileConnectionContextManager;
import com.dbn.database.interfaces.DatabaseCompatibilityInterface;
import com.dbn.database.interfaces.DatabaseInterfaceInvoker;
import com.dbn.database.interfaces.DatabaseMetadataInterface;
import com.dbn.execution.ExecutionManager;
import com.dbn.execution.explain.result.ExplainPlanResult;
import com.dbn.language.common.DBLanguagePsiFile;
import com.dbn.language.common.psi.ExecutablePsiElement;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.dbn.common.Priority.HIGH;
import static com.dbn.common.component.Components.projectService;
import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

public class ExplainPlanManager extends ProjectComponentBase {
    public static final String COMPONENT_NAME = "DBNavigator.Project.ExplainPlanManager";

    private ExplainPlanManager(Project project) {
        super(project, COMPONENT_NAME);
        //EventManager.subscribe(project, PsiDocumentTransactionListener.TOPIC, psiDocumentTransactionListener);
    }

    public static ExplainPlanManager getInstance(@NotNull Project project) {
        return projectService(project, ExplainPlanManager.class);
    }

    @Override
    public void disposeInner() {
        //EventManager.unsubscribe(psiDocumentTransactionListener);
        super.disposeInner();
    }

    /*********************************************************
     *                       Execution                       *
     *********************************************************/

    public void executeExplainPlan(
            @NotNull ExecutablePsiElement executable,
            @NotNull DataContext dataContext,
            @Nullable Consumer<ExplainPlanResult> callback) {

        Project project = getProject();
        String elementDescription = executable.getSpecificElementType().getDescription();

        DBLanguagePsiFile databaseFile = executable.getFile();
        FileConnectionContextManager contextManager = FileConnectionContextManager.getInstance(project);
        contextManager.selectConnectionAndSchema(
                databaseFile.getVirtualFile(),
                dataContext,
                ()-> ConnectionAction.invoke("generating the explain plan", false, executable,
                        action -> Progress.prompt(getProject(), action, true,
                                "Extracting explain plan",
                                "Extracting explain plan for " + elementDescription,
                                progress -> {
                                    ConnectionHandler connection = action.getConnection();
                                    ExplainPlanResult explainPlanResult = createExplainPlan(executable, connection);

                                    if (callback == null) {
                                        ExecutionManager executionManager = ExecutionManager.getInstance(project);
                                        executionManager.addExplainPlanResult(explainPlanResult);
                                    } else {
                                        callback.accept(explainPlanResult);
                                    }
                                })));
    }

    private static ExplainPlanResult createExplainPlan(@NotNull ExecutablePsiElement executable, ConnectionHandler connection) {
        try {
            return DatabaseInterfaceInvoker.load(HIGH,
                    "Creating explain plan",
                    "Running explain plan for SQL statement",
                    connection.getProject(),
                    connection.getConnectionId(),
                    conn -> {
                        SchemaId currentSchema = executable.getFile().getSchemaId();
                        connection.setCurrentSchema(conn, currentSchema);
                        Statement statement = null;
                        ResultSet resultSet = null;
                        try {
                            DatabaseMetadataInterface metadata = connection.getMetadataInterface();
                            metadata.clearExplainPlanData(conn);

                            DatabaseCompatibilityInterface compatibility = connection.getCompatibilityInterface();
                            String explainPlanStatementPrefix = compatibility.getExplainPlanStatementPrefix();
                            String explainPlanQuery = explainPlanStatementPrefix + "\n" + executable.prepareStatementText();
                            statement = conn.createStatement();
                            statement.setFetchSize(500);
                            statement.execute(explainPlanQuery);

                            resultSet = metadata.loadExplainPlan(conn);
                            return new ExplainPlanResult(executable, resultSet);

                        } finally {
                            Resources.close(resultSet);
                            Resources.close(statement);
                            Resources.rollbackSilently(conn);
                        }
                    });
        } catch (SQLException e) {
            conditionallyLog(e);
            return new ExplainPlanResult(executable, e.getMessage());
        }
    }
}
