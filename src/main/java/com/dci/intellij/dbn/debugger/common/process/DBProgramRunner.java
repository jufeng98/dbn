package com.dci.intellij.dbn.debugger.common.process;

import com.dci.intellij.dbn.common.event.ProjectEvents;
import com.dci.intellij.dbn.common.notification.NotificationGroup;
import com.dci.intellij.dbn.common.notification.NotificationSupport;
import com.dci.intellij.dbn.common.thread.Dispatch;
import com.dci.intellij.dbn.common.thread.Progress;
import com.dci.intellij.dbn.connection.ConnectionAction;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.config.ConnectionDebuggerSettings;
import com.dci.intellij.dbn.debugger.DatabaseDebuggerManager;
import com.dci.intellij.dbn.debugger.common.config.DBRunConfig;
import com.dci.intellij.dbn.debugger.common.config.ui.CompileDebugDependenciesDialog;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.execution.ExecutionInput;
import com.dci.intellij.dbn.execution.compiler.*;
import com.dci.intellij.dbn.object.DBMethod;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionManager;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.GenericProgramRunner;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ExecutionConsole;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.RunContentManager;
import com.intellij.history.LocalHistory;
import com.intellij.openapi.project.Project;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.dci.intellij.dbn.common.message.MessageCallback.when;
import static com.dci.intellij.dbn.common.util.Messages.options;
import static com.dci.intellij.dbn.common.util.Messages.showWarningDialog;
import static com.dci.intellij.dbn.common.util.Unsafe.cast;
import static com.dci.intellij.dbn.diagnostics.Diagnostics.conditionallyLog;
import static com.intellij.openapi.ui.DialogWrapper.OK_EXIT_CODE;

public abstract class DBProgramRunner<T extends ExecutionInput> extends GenericProgramRunner {
    public static final String INVALID_RUNNER_ID = "DBNInvalidRunner";
    public static final String CLOUD_DATABASE_PATTERN = ".+\\.ade\\..+\\.oraclecloud\\.com";
    public String hostnames = System.getProperty("cloud.hostnames");

    @Nullable
    @Override
    protected RunContentDescriptor doExecute(@NotNull RunProfileState state, @NotNull ExecutionEnvironment environment) throws ExecutionException {
        return doExecute(environment.getProject(), environment.getExecutor(), state, null, environment);
    }

    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        if (!Objects.equals(executorId, DefaultDebugExecutor.EXECUTOR_ID)) return false;

        if (profile instanceof DBRunConfig) {
            DBRunConfig runConfiguration = (DBRunConfig) profile;
            return runConfiguration.canRun();
        }
        return false;
    }

    @Override
    @Nullable
    @SneakyThrows
    protected RunContentDescriptor doExecute(@NotNull Project project, @NotNull RunProfileState state, RunContentDescriptor contentToReuse, @NotNull ExecutionEnvironment env) {
        return doExecute(project, env.getExecutor(), state, contentToReuse, env);
    }

    private RunContentDescriptor doExecute(
            Project project,
            Executor executor,
            RunProfileState state,
            RunContentDescriptor contentToReuse,
            ExecutionEnvironment environment) throws ExecutionException {

        DBRunConfig runProfile = (DBRunConfig) environment.getRunProfile();
        ConnectionHandler connection = runProfile.getConnection();
        if (connection == null) return null;

        ConnectionAction.invoke("the debug execution", false, connection,
                action -> Progress.prompt(project, connection, true,
                        "Checking debug privileges",
                        "Checking debug privileges for user \"" + connection.getUserName() + "\"",
                        progress -> performPrivilegeCheck(
                                project,
                                cast(runProfile.getExecutionInput()),
                                environment,
                                null)),
                null,
                action -> {
                    DatabaseDebuggerManager databaseDebuggerManager = DatabaseDebuggerManager.getInstance(project);
                    return databaseDebuggerManager.checkForbiddenOperation(connection,
                            "Another debug session is active on this connection. You can only run one debug session at the time.");
                });
        return null;
    }

    private void performPrivilegeCheck(
            Project project,
            T executionInput,
            ExecutionEnvironment environment,
            Callback callback) {
        DBRunConfig runProfile = (DBRunConfig) environment.getRunProfile();
        ConnectionHandler connection = runProfile.getConnection();
        if (connection == null) return;

        DatabaseDebuggerManager debuggerManager = DatabaseDebuggerManager.getInstance(project);
        List<String> missingPrivileges = debuggerManager.getMissingDebugPrivileges(connection);
        if (missingPrivileges.isEmpty()) {
            performInitialization(
                    connection,
                    executionInput,
                    environment,
                    callback);
        } else {
            StringBuilder buffer = new StringBuilder();
            buffer.append("The current user (").append(connection.getUserName()).append(") does not have sufficient privileges to perform debug operations on this database.\n");
            buffer.append("Please contact your administrator to grant the required privileges. ");
            buffer.append("Missing privileges:\n");
            for (String missingPrivilege : missingPrivileges) {
                buffer.append(" - ").append(missingPrivilege).append("\n");
            }

            showWarningDialog(
                    project, "Insufficient privileges", buffer.toString(),
                    options("Continue anyway", "Cancel"), 0,
                    option -> when(option == 0, () ->
                            performInitialization(
                                    connection,
                                    executionInput,
                                    environment,
                                    callback)));
        }
    }

    private void performInitialization(
            @NotNull ConnectionHandler connection,
            @NotNull T executionInput,
            @NotNull ExecutionEnvironment environment,
            @Nullable Callback callback) {

        ConnectionDebuggerSettings debuggerSettings = connection.getSettings().getDebuggerSettings();
        if (!debuggerSettings.isCompileDependencies()) return;

        Project project = connection.getProject();
        Progress.prompt(project, connection, true,
                "Initializing debug environment",
                "Loading method dependencies",
                progress -> {
                    if (progress.isCanceled()) return;
                    DatabaseDebuggerManager debuggerManager = DatabaseDebuggerManager.getInstance(project);

                    DBRunConfig<?> runProfile = (DBRunConfig) environment.getRunProfile();
                    List<DBMethod> methods = runProfile.getMethods();
                    List<DBSchemaObject> dependencies = debuggerManager.loadCompileDependencies(methods);
                    if (progress.isCanceled()) return;

                    if (dependencies.isEmpty()) {
                        performExecution(
                                executionInput,
                                environment,
                                callback);
                    } else {
                        performCompile(
                                connection,
                                executionInput,
                                environment,
                                callback,
                                dependencies);
                    }
                });
    }

    private void performCompile(
            @NotNull ConnectionHandler connection,
            @NotNull T executionInput,
            @NotNull ExecutionEnvironment environment,
            @Nullable Callback callback,
            List<DBSchemaObject> dependencies) {

        Dispatch.run(() -> {
            Project project = connection.getProject();
            DBRunConfig runConfiguration = (DBRunConfig) environment.getRunProfile();
            CompileDebugDependenciesDialog dependenciesDialog = new CompileDebugDependenciesDialog(runConfiguration, dependencies);
            dependenciesDialog.show();

            DBObjectRef<DBSchemaObject>[] selectedDependencies =  dependenciesDialog.getSelection();
            if (dependenciesDialog.getExitCode() != OK_EXIT_CODE) return;

            if (selectedDependencies.length > 0) {
                Progress.prompt(project, connection, true,
                        "Compiling dependencies",
                        "Compiling dependencies for program execution",
                        progress -> {
                    DatabaseCompilerManager compilerManager = DatabaseCompilerManager.getInstance(project);
                    for (DBObjectRef<DBSchemaObject> objectRef : selectedDependencies) {
                        DBSchemaObject schemaObject = objectRef.ensure();
                        progress.checkCanceled();

                        progress.setText2("Compiling " + objectRef.getQualifiedNameWithType());
                        DBContentType contentType = schemaObject.getContentType();
                        CompilerAction compilerAction = new CompilerAction(CompilerActionSource.BULK_COMPILE, contentType);
                        compilerManager.compileObject(schemaObject, CompileType.DEBUG, compilerAction);
                    }
                    ProjectEvents.notify(project,
                            CompileManagerListener.TOPIC,
                            (listener) -> listener.compileFinished(connection, null));
                            progress.checkCanceled();

                    performExecution(
                            executionInput,
                            environment,
                            callback);
                });
            } else {
                performExecution(
                        executionInput,
                        environment,
                        callback);
            }
        });
    }

    private void performExecution(
            T executionInput,
            ExecutionEnvironment environment,
            Callback callback) {
        Dispatch.run(() ->
                promptExecutionDialog(executionInput, () ->
                        triggerExecution(executionInput, environment, callback)));
    }

    private void triggerExecution(T executionInput, ExecutionEnvironment environment, Callback callback) {
        ConnectionHandler connection = executionInput.getConnection();
        Project project = environment.getProject();

        DBDebugProcessStarter processStarter = createProcessStarter(connection);
        try {
            XDebuggerManager debuggerManager = XDebuggerManager.getInstance(project);
            XDebugSession session = debuggerManager.startSession(environment, processStarter);

            RunContentDescriptor descriptor = session.getRunContentDescriptor();

            if (callback != null) callback.processStarted(descriptor);
            Executor executor = environment.getExecutor();
            if (true /*LocalHistoryConfiguration.getInstance().ADD_LABEL_ON_RUNNING*/) {
                RunProfile runProfile = environment.getRunProfile();
                LocalHistory.getInstance().putSystemLabel(project, executor.getId() + " " + runProfile.getName());
            }

            ExecutionManager executionManager = ExecutionManager.getInstance(project);
            RunContentManager contentManager = executionManager.getContentManager();
            contentManager.showRunContent(executor, descriptor);

            ProcessHandler processHandler = descriptor.getProcessHandler();
            if (processHandler == null) return;
            if (!processHandler.isStartNotified()) processHandler.startNotify();

            ExecutionConsole executionConsole = descriptor.getExecutionConsole();
            if (executionConsole instanceof ConsoleView) {
                ConsoleView consoleView = (ConsoleView) executionConsole;
                consoleView.attachToProcess(processHandler);
            }

        } catch (ExecutionException e) {
            conditionallyLog(e);
            NotificationSupport.sendErrorNotification(
                    project,
                    NotificationGroup.DEBUGGER,
                    "Error initializing environment: {0}", e);
        }
    }
    protected   boolean isCloudDatabaseDefaultValue(ConnectionHandler conn)  {

        String databaseHost = conn.getDatabaseInfo().getHost();
        try{
            boolean isCloud = databaseHost.matches(CLOUD_DATABASE_PATTERN);
            if(!isCloud && hostnames != null && !hostnames.isEmpty()){
                String [] hostnamesArray = hostnames.split(",");
                isCloud = Arrays.asList(hostnamesArray).contains(databaseHost);
            }
            if ( !isCloud &&( databaseHost.equals("localhost") || databaseHost.equals("127.0.0.1") ||databaseHost.equals( InetAddress.getLocalHost().getHostAddress()))) {
                return isCloud = false;
            }
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }

        return  true;
    }
    protected abstract DBDebugProcessStarter createProcessStarter(ConnectionHandler connection);

    protected abstract void promptExecutionDialog(T executionInput, Runnable callback);
}
