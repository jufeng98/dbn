package com.dci.intellij.dbn.connection;

import com.dci.intellij.dbn.DatabaseNavigator;
import com.dci.intellij.dbn.browser.DatabaseBrowserManager;
import com.dci.intellij.dbn.common.component.PersistentState;
import com.dci.intellij.dbn.common.component.ProjectComponentBase;
import com.dci.intellij.dbn.common.component.ProjectManagerListener;
import com.dci.intellij.dbn.common.database.AuthenticationInfo;
import com.dci.intellij.dbn.common.database.DatabaseInfo;
import com.dci.intellij.dbn.common.dispose.Disposer;
import com.dci.intellij.dbn.common.environment.EnvironmentType;
import com.dci.intellij.dbn.common.event.ProjectEvents;
import com.dci.intellij.dbn.common.message.MessageCallback;
import com.dci.intellij.dbn.common.routine.Consumer;
import com.dci.intellij.dbn.common.thread.Background;
import com.dci.intellij.dbn.common.thread.Dispatch;
import com.dci.intellij.dbn.common.thread.Progress;
import com.dci.intellij.dbn.common.util.*;
import com.dci.intellij.dbn.connection.config.ConnectionConfigListener;
import com.dci.intellij.dbn.connection.config.ConnectionDatabaseSettings;
import com.dci.intellij.dbn.connection.config.ConnectionSettings;
import com.dci.intellij.dbn.connection.config.file.DatabaseFileBundle;
import com.dci.intellij.dbn.connection.info.ConnectionInfo;
import com.dci.intellij.dbn.connection.info.ui.ConnectionInfoDialog;
import com.dci.intellij.dbn.connection.jdbc.DBNConnection;
import com.dci.intellij.dbn.connection.jdbc.ResourceStatus;
import com.dci.intellij.dbn.connection.mapping.FileConnectionContextManager;
import com.dci.intellij.dbn.connection.transaction.DatabaseTransactionManager;
import com.dci.intellij.dbn.connection.transaction.TransactionAction;
import com.dci.intellij.dbn.connection.transaction.ui.IdleConnectionDialog;
import com.dci.intellij.dbn.connection.ui.ConnectionAuthenticationDialog;
import com.dci.intellij.dbn.execution.ExecutionManager;
import com.dci.intellij.dbn.execution.method.MethodExecutionManager;
import com.dci.intellij.dbn.options.ConfigId;
import com.dci.intellij.dbn.options.ProjectSettingsManager;
import com.dci.intellij.dbn.vfs.DatabaseFileManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import lombok.extern.slf4j.Slf4j;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.dci.intellij.dbn.common.component.Components.projectService;
import static com.dci.intellij.dbn.common.dispose.Checks.isNotValid;
import static com.dci.intellij.dbn.common.dispose.Failsafe.guarded;
import static com.dci.intellij.dbn.common.message.MessageCallback.when;
import static com.dci.intellij.dbn.common.util.Messages.*;
import static com.dci.intellij.dbn.connection.transaction.TransactionAction.actions;
import static com.dci.intellij.dbn.diagnostics.Diagnostics.conditionallyLog;

@State(
    name = ConnectionManager.COMPONENT_NAME,
    storages = @Storage(DatabaseNavigator.STORAGE_FILE)
)
@Slf4j
public class ConnectionManager extends ProjectComponentBase implements PersistentState, ProjectManagerListener {

    public static final String COMPONENT_NAME = "DBNavigator.Project.ConnectionManager";

    private final Timer idleConnectionCleaner;
    private final ConnectionBundle connectionBundle;
    private static ConnectionRef lastUsedConnection;

    public static ConnectionManager getInstance(@NotNull Project project) {
        return projectService(project, ConnectionManager.class);
    }

    private ConnectionManager(@NotNull Project project) {
        super(project, COMPONENT_NAME);
        connectionBundle = new ConnectionBundle(project);

        ProjectEvents.subscribe(project, this,
                ConnectionConfigListener.TOPIC,
                ConnectionConfigListener.whenChanged(id -> refreshObjects(id)));

        idleConnectionCleaner = new Timer("DBN - Idle Connection Cleaner");
        idleConnectionCleaner.schedule(new CloseIdleConnectionTask(), TimeUtil.Millis.ONE_MINUTE, TimeUtil.Millis.ONE_MINUTE);

        Disposer.register(this, connectionBundle);
    }

    @Override
    public void projectClosed() {
        ConnectionCache.releaseCache(getProject());
    }

    @Override
    public void disposeInner() {
        Disposer.dispose(idleConnectionCleaner);
        super.disposeInner();
    }

    @Nullable
    public static ConnectionHandler getLastUsedConnection() {
        return lastUsedConnection == null ? null : lastUsedConnection.get();
    }

    @Nullable
    public static ConnectionInfo getLastUsedConnectionInfo() {
        ConnectionHandler lastUsedConnection = getLastUsedConnection();
        return lastUsedConnection == null ? null : lastUsedConnection.getConnectionInfo();
    }

    static void setLastUsedConnection(@NotNull ConnectionHandler lastUsedConnection) {
        ConnectionManager.lastUsedConnection = lastUsedConnection.ref();
    }

    private void refreshObjects(ConnectionId connectionId) {
        ConnectionHandler connection = getConnection(connectionId);
        if (connection == null) return;

        Project project = getProject();
        Background.run(project, () -> {
            connection.resetCompatibilityMonitor();
            List<TransactionAction> actions = actions(TransactionAction.DISCONNECT);

            DatabaseTransactionManager transactionManager = DatabaseTransactionManager.getInstance(project);
            List<DBNConnection> connections = connection.getConnections();
            for (DBNConnection conn : connections) {
                transactionManager.execute(connection, conn, actions, false, null);
            }
            connection.getObjectBundle().getObjectLists().refreshObjects();
        });
    }

    /*********************************************************
    *                        Custom                         *
    *********************************************************/
    public ConnectionBundle getConnectionBundle() {
        return connectionBundle;
    }

    public static void testConnection(ConnectionHandler connection, SchemaId schemaId, SessionId sessionId, boolean showSuccessMessage, boolean showErrorMessage) {
        Project project = connection.getProject();
        Progress.prompt(project, connection, true,
                "Testing connection",
                "Trying to connect to " + connection.getQualifiedName(),
                progress -> {
                    ConnectionDatabaseSettings databaseSettings = connection.getSettings().getDatabaseSettings();
                    String connectionName = connection.getName();
                    try {
                        databaseSettings.validate();
                        connection.getConnection(sessionId, schemaId);
                        ConnectionHandlerStatusHolder connectionStatus = connection.getConnectionStatus();
                        connectionStatus.setValid(true);
                        connectionStatus.setConnected(true);
                        if (showSuccessMessage) {
                            showSuccessfulConnectionMessage(project, connectionName);
                        }
                    } catch (ConfigurationException e) {
                        conditionallyLog(e);
                        if (showErrorMessage) {
                            showInvalidConfigMessage(project, e);

                        }
                    } catch (Exception e) {
                        conditionallyLog(e);
                        if (showErrorMessage) {
                            showErrorConnectionMessage(project, connectionName, e);
                        }
                    }
                });

    }

    public void testConfigConnection(ConnectionSettings connectionSettings, boolean showMessageDialog) {
        Project project = connectionSettings.getProject();
        ConnectionDatabaseSettings databaseSettings = connectionSettings.getDatabaseSettings();
        try {
            databaseSettings.validate();

            if (databaseSettings.isDatabaseInitialized()) {
                ensureAuthenticationProvided(databaseSettings, (authenticationInfo) ->
                        attemptConfigConnection(
                                connectionSettings,
                                authenticationInfo,
                                showMessageDialog));
            } else {
                promptDatabaseInitDialog(databaseSettings,
                        option -> when(option == 0, () ->
                                ensureAuthenticationProvided(databaseSettings,
                                        authInfo -> attemptConfigConnection(
                                                connectionSettings,
                                                authInfo,
                                                showMessageDialog))));
            }




        } catch (ConfigurationException e) {
            conditionallyLog(e);
            showInvalidConfigMessage(project, e);
        }
    }

    private static void attemptConfigConnection(ConnectionSettings connectionSettings, AuthenticationInfo authentication, boolean showMessageDialog) {
        Project project = connectionSettings.getProject();
        ConnectionDatabaseSettings databaseSettings = connectionSettings.getDatabaseSettings();
        String connectionName = databaseSettings.getName();

        Progress.modal(project, null, false,
                "Connecting to database",
                "Trying to connect to " + connectionName,
                progress -> {
            try {
                DBNConnection connection = ConnectionUtil.connect(connectionSettings, null, authentication, SessionId.TEST, false, null);
                Resources.close(connection);
                databaseSettings.setConnectivityStatus(ConnectivityStatus.VALID);
                if (showMessageDialog) {
                    showSuccessfulConnectionMessage(project, connectionName);
                }
            } catch (Exception e) {
                conditionallyLog(e);
                databaseSettings.setConnectivityStatus(ConnectivityStatus.INVALID);
                if (showMessageDialog) {
                    showErrorConnectionMessage(project, connectionName, e);
                }
            }
        });
    }

    public void showConnectionInfo(ConnectionSettings connectionSettings, EnvironmentType environmentType) {
        ConnectionDatabaseSettings databaseSettings = connectionSettings.getDatabaseSettings();
        String connectionName = databaseSettings.getName();
        Project project = connectionSettings.getProject();

        try {
            databaseSettings.validate();
            ensureAuthenticationProvided(databaseSettings, (authenticationInfo) ->
                    Progress.modal(project, null, false,
                            "Connecting to database",
                            "Connecting to " + connectionName,
                            progress -> {
                                try {
                                    DBNConnection connection = ConnectionUtil.connect(connectionSettings, null, authenticationInfo, SessionId.TEST, false, null);
                                    ConnectionInfo connectionInfo = new ConnectionInfo(connection.getMetaData());
                                    Resources.close(connection);
                                    showConnectionInfoDialog(connectionInfo, connectionName, environmentType);
                                } catch (Exception e) {
                                    conditionallyLog(e);
                                    showErrorConnectionMessage(project, connectionName, e);
                                }
                            }));

        } catch (ConfigurationException e) {
            conditionallyLog(e);
            showInvalidConfigMessage(project, e);
        }
    }

    public void promptMissingConnection() {
        Project project = getProject();
        showInfoDialog(
                project, "No connections available.", "No active database connections found. Please setup or activate a connection first",
                new String[]{"Setup Connection", "Cancel"}, 0,
                option -> when(option == 0, () -> {
                    ProjectSettingsManager settingsManager = ProjectSettingsManager.getInstance(project);
                    settingsManager.openProjectSettings(ConfigId.CONNECTIONS);
                }));
    }

    private void ensureAuthenticationProvided(
            @NotNull ConnectionDatabaseSettings databaseSettings,
            @NotNull Consumer<AuthenticationInfo> consumer) {

        AuthenticationInfo authenticationInfo = databaseSettings.getAuthenticationInfo().clone();
        if (!authenticationInfo.isProvided()) {
            promptAuthenticationDialog(null, authenticationInfo, consumer);
        } else {
            consumer.accept(authenticationInfo);
        }
    }

    static void promptDatabaseInitDialog(ConnectionHandler connection, MessageCallback callback) {
        ConnectionDatabaseSettings databaseSettings = connection.getSettings().getDatabaseSettings();
        promptDatabaseInitDialog(databaseSettings, callback);
    }

    private static void promptDatabaseInitDialog(ConnectionDatabaseSettings databaseSettings, MessageCallback callback) {
        DatabaseInfo databaseInfo = databaseSettings.getDatabaseInfo();
        if (databaseInfo.getUrlType() == DatabaseUrlType.FILE) {
            DatabaseFileBundle fileBundle = databaseInfo.getFileBundle();
            Project project = databaseSettings.getProject();
            if (fileBundle == null || fileBundle.isEmpty()) {
                showErrorDialog(project, "Wrong database configuration", "Database file not specified");
            } else {
                String missingFiles = fileBundle
                        .getFiles()
                        .stream()
                        .filter(f -> f.isValid() && !f.isPresent())
                        .map(f -> f.getPath())
                        .collect(Collectors.joining("\n"));

                if (!Strings.isEmpty(missingFiles)) {
                    showWarningDialog(
                            project,
                            "Database file not available",
                            "Following database files not exist.\n" + missingFiles + "\nDo you want to create them?",
                            options("Create", "Cancel"), 0,
                            callback);
                }
            }
        }
    }

    static void promptConnectDialog(ConnectionHandler connection, @Nullable String actionDesc, MessageCallback callback) {
        showInfoDialog(
                connection.getProject(),
                "Not connected to database",
                "You are not connected to database \"" + connection.getName() + "\". \n" +
                        "If you want to continue" + (actionDesc == null ? "" : " with " + actionDesc) + ", you need to connect.",
                ConnectionAction.OPTIONS_CONNECT_CANCEL, 0,
                callback);
    }

    static void showErrorConnectionMessage(Project project, String connectionName, Throwable e) {
        showErrorDialog(
                project,
                "Connection error",
                "Cannot connect to \"" + connectionName + "\".\n" + (e == null ? "Unknown reason" : e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage()));
    }

    private static void showSuccessfulConnectionMessage(Project project, String connectionName) {
        showInfoDialog(
                project,
                "Connection successful",
                "Connection to \"" + connectionName + "\" was successful.");
    }

    private static void showInvalidConfigMessage(Project project, ConfigurationException e) {
        showErrorDialog(
                project,
                "Invalid configuration",
                e.getMessage());
    }

    public static void showConnectionInfoDialog(ConnectionHandler connection) {
        Dialogs.show(() -> new ConnectionInfoDialog(connection));
    }

    private static void showConnectionInfoDialog(ConnectionInfo connectionInfo, String connectionName, EnvironmentType environmentType) {
        Dialogs.show(() -> new ConnectionInfoDialog(null, connectionInfo, connectionName, environmentType));
    }

    void promptAuthenticationDialog(
            @Nullable ConnectionHandler connection,
            @NotNull AuthenticationInfo authenticationInfo,
            @NotNull Consumer<AuthenticationInfo> consumer) {

        Dialogs.show(
                () -> new ConnectionAuthenticationDialog(getProject(), connection, authenticationInfo),
                (dialog, exitCode) -> {
                    if (exitCode != DialogWrapper.OK_EXIT_CODE) return;

                    AuthenticationInfo newAuthenticationInfo = dialog.getAuthenticationInfo();
                    if (connection != null) {
                        AuthenticationInfo storedAuthenticationInfo = connection.getAuthenticationInfo();

                        if (dialog.isRememberCredentials()) {
                            String oldUser = storedAuthenticationInfo.getUser();
                            String oldPassword = storedAuthenticationInfo.getPassword();

                            storedAuthenticationInfo.setUser(newAuthenticationInfo.getUser());
                            storedAuthenticationInfo.setPassword(newAuthenticationInfo.getPassword());
                            storedAuthenticationInfo.setType(newAuthenticationInfo.getType());

                            storedAuthenticationInfo.updateKeyChain(oldUser, oldPassword);
                        } else {
                            AuthenticationInfo temporaryAuthenticationInfo = newAuthenticationInfo.clone();
                            temporaryAuthenticationInfo.setTemporary(true);
                            connection.setTemporaryAuthenticationInfo(temporaryAuthenticationInfo);
                        }
                        connection.getInstructions().setAllowAutoConnect(true);
                    }
                    consumer.accept(newAuthenticationInfo);
                });
    }

    /*********************************************************
     *                     Miscellaneous                     *
     *********************************************************/
    @Nullable
    public ConnectionHandler getConnection(ConnectionId connectionId) {
        return getConnectionBundle().getConnection(connectionId);
     }

     public List<ConnectionHandler> getConnections(Predicate<ConnectionHandler> predicate) {
        return Lists.filtered(getConnections(), predicate);
     }

     public List<ConnectionHandler> getConnections() {
         return getConnectionBundle().getConnections();
     }

     public ConnectionHandler getActiveConnection(Project project) {
         ConnectionHandler connection = null;
         VirtualFile virtualFile = Editors.getSelectedFile(project);
         DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(project);
         if (browserManager.getBrowserToolWindow().isActive() || virtualFile == null) {
             connection = browserManager.getActiveConnection();
         }

         if (connection == null && virtualFile != null) {
             FileConnectionContextManager contextManager = FileConnectionContextManager.getInstance(project);
             connection = contextManager.getConnection(virtualFile);
         }

         return connection;
     }

    public boolean hasUncommittedChanges() {
        for (ConnectionHandler connection : getConnections()) {
            if (connection.hasUncommittedChanges()) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidConnectionId(ConnectionId connectionId) {
        return getConnection(connectionId) != null;
    }

    private class CloseIdleConnectionTask extends TimerTask {
        @Override
        public void run() {
            try {
                if (isNotValid(ConnectionManager.this.getProject())) return;
                for (ConnectionHandler connection : getConnections()) {
                    resolveIdleStatus(connection);
                }
            } catch (Exception e){
                conditionallyLog(e);
                log.error("Failed to release idle connections", e);
            }
        }

        private void resolveIdleStatus(ConnectionHandler connection) {
            guarded(connection, c -> {
                if (isNotValid(c) || isNotValid(c.getProject())) return;

                List<TransactionAction> actions = actions(TransactionAction.DISCONNECT_IDLE);
                DatabaseTransactionManager transactionManager = DatabaseTransactionManager.getInstance(getProject());
                List<DBNConnection> connections = c.getConnections(ConnectionType.MAIN, ConnectionType.SESSION);

                for (DBNConnection conn : connections) {
                    if (!conn.isIdle()) continue;
                    if (conn.is(ResourceStatus.RESOLVING_TRANSACTION)) continue;

                    int idleMinutes = conn.getIdleMinutes();
                    int idleMinutesToDisconnect = c.getSettings().getDetailSettings().getIdleMinutesToDisconnect();
                    if (idleMinutes > idleMinutesToDisconnect) {
                        if (conn.hasDataChanges()) {
                            conn.set(ResourceStatus.RESOLVING_TRANSACTION, true);
                            Dialogs.show(() -> new IdleConnectionDialog(c, conn));
                        } else {
                            transactionManager.execute(c, conn, actions, false, null);
                        }
                    }
                }
            });
        }
    }

    void disposeConnections(@NotNull List<ConnectionHandler> connections) {
        if (connections.isEmpty()) return;

        Dispatch.run(() -> {
            Project project = getProject();
            List<ConnectionId> connectionIds = ConnectionHandler.ids(connections);

            ExecutionManager executionManager = ExecutionManager.getInstance(project);
            executionManager.closeExecutionResults(connectionIds);

            DatabaseFileManager databaseFileManager = DatabaseFileManager.getInstance(project);
            databaseFileManager.closeDatabaseFiles(connectionIds);

            MethodExecutionManager methodExecutionManager = MethodExecutionManager.getInstance(project);
            methodExecutionManager.cleanupExecutionHistory(connectionIds);

            Background.run(project, () -> {
                for (ConnectionHandler connection : connections) {
                    connection.getConnectionPool().closeConnections();
                    Disposer.dispose(connection);
                }
            });
        });
    }

    /*********************************************************
     *                PersistentStateComponent               *
     *********************************************************/
    @Override
    @Nullable
    public Element getComponentState() {
        return null;
    }

    @Override
    public void loadComponentState(@NotNull Element element) {}
}