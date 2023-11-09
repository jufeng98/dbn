package com.dci.intellij.dbn.connection;

import com.dci.intellij.dbn.browser.model.BrowserTreeNode;
import com.dci.intellij.dbn.common.Referenceable;
import com.dci.intellij.dbn.common.cache.Cache;
import com.dci.intellij.dbn.common.database.AuthenticationInfo;
import com.dci.intellij.dbn.common.database.DatabaseInfo;
import com.dci.intellij.dbn.common.dispose.StatefulDisposable;
import com.dci.intellij.dbn.common.environment.EnvironmentTypeProvider;
import com.dci.intellij.dbn.common.filter.Filter;
import com.dci.intellij.dbn.common.thread.ThreadMonitor;
import com.dci.intellij.dbn.common.ui.Presentable;
import com.dci.intellij.dbn.common.util.Lists;
import com.dci.intellij.dbn.connection.config.ConnectionSettings;
import com.dci.intellij.dbn.connection.console.DatabaseConsoleBundle;
import com.dci.intellij.dbn.connection.context.DatabaseContextBase;
import com.dci.intellij.dbn.connection.info.ConnectionInfo;
import com.dci.intellij.dbn.connection.jdbc.DBNConnection;
import com.dci.intellij.dbn.connection.session.DatabaseSessionBundle;
import com.dci.intellij.dbn.database.DatabaseCompatibility;
import com.dci.intellij.dbn.database.interfaces.DatabaseInterfaceQueue;
import com.dci.intellij.dbn.execution.statement.StatementExecutionQueue;
import com.dci.intellij.dbn.language.common.DBLanguage;
import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.vfs.file.DBSessionBrowserVirtualFile;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

import static com.dci.intellij.dbn.common.dispose.Checks.isNotValid;
import static com.dci.intellij.dbn.common.dispose.Failsafe.nd;

public interface ConnectionHandler extends StatefulDisposable, EnvironmentTypeProvider, DatabaseContextBase, Presentable, Referenceable<ConnectionRef> {

    @NotNull
    Project getProject();

    @NotNull
    ConnectionId getConnectionId();

    DBNConnection getTestConnection() throws SQLException;

    @NotNull
    DBNConnection getMainConnection() throws SQLException;

    @NotNull
    DBNConnection getMainConnection(@Nullable SchemaId schemaId) throws SQLException;

    @NotNull
    DBNConnection getConnection(@NotNull SessionId sessionId) throws SQLException;

    @NotNull
    DBNConnection getConnection(@NotNull SessionId sessionId, @Nullable SchemaId schemaId) throws SQLException;

    @NotNull
    DBNConnection getDebugConnection(@Nullable SchemaId schemaId) throws SQLException;

    @NotNull
    DBNConnection getDebuggerConnection() throws SQLException;

    @NotNull
    DBNConnection getPoolConnection(boolean readonly) throws SQLException;

    @NotNull
    DBNConnection getPoolConnection(@Nullable SchemaId schemaId, boolean readonly) throws SQLException;

    void setCurrentSchema(DBNConnection connection, @Nullable SchemaId schema) throws SQLException;

    void closeConnection(DBNConnection connection);

    void freePoolConnection(DBNConnection connection);

    @NotNull
    ConnectionSettings getSettings();

    void setSettings(ConnectionSettings connectionSettings);

    @NotNull
    List<DBNConnection> getConnections(ConnectionType... connectionTypes);

    @NotNull
    ConnectionHandlerStatusHolder getConnectionStatus();

    @NotNull
    DatabaseConsoleBundle getConsoleBundle();

    @NotNull
    DatabaseSessionBundle getSessionBundle();

    @NotNull
    DBSessionBrowserVirtualFile getSessionBrowserFile();

    ConnectionInstructions getInstructions();

    void setTemporaryAuthenticationInfo(AuthenticationInfo temporaryAuthenticationInfo);

    @Nullable
    ConnectionInfo getConnectionInfo();

    default Cache getMetaDataCache() {
        return null;
    }

    @NotNull
    String getConnectionName(@Nullable DBNConnection connection);

    void setConnectionInfo(ConnectionInfo connectionInfo);

    @NotNull
    AuthenticationInfo getTemporaryAuthenticationInfo();

    boolean canConnect();

    boolean isAuthenticationProvided();

    boolean isDatabaseInitialized();

    @NotNull
    ConnectionBundle getConnectionBundle();

    @NotNull
    ConnectionPool getConnectionPool();

    DatabaseInterfaceQueue getInterfaceQueue();

    @Nullable
    SchemaId getUserSchema();

    @Nullable
    SchemaId getDefaultSchema();

    @NotNull
    List<SchemaId> getSchemaIds();

    @Nullable
    SchemaId getSchemaId(String name);

    @Nullable
    DBSchema getSchema(SchemaId schema);

    boolean isValid();

    boolean isVirtual();

    boolean isAutoCommit();

    boolean isLoggingEnabled();

    boolean hasPendingTransactions(@NotNull DBNConnection conn);

    void setAutoCommit(boolean autoCommit);

    void setLoggingEnabled(boolean loggingEnabled);

    void disconnect() throws SQLException;

    String getUserName();

    @Nullable
    DBLanguageDialect resolveLanguageDialect(Language language);

    DBLanguageDialect getLanguageDialect(DBLanguage language);

    boolean isEnabled();

    DatabaseType getDatabaseType();

    double getDatabaseVersion();

    Filter<BrowserTreeNode> getObjectTypeFilter();

    boolean isConnected();

    boolean isConnected(SessionId sessionId);

    ConnectionRef ref();

    DatabaseInfo getDatabaseInfo();

    AuthenticationInfo getAuthenticationInfo();

    @Deprecated
    boolean hasUncommittedChanges();

    @Nullable
    StatementExecutionQueue getExecutionQueue(SessionId sessionId);

    @NotNull
    PsiDirectory getPsiDirectory();

    static List<ConnectionId> ids(List<ConnectionHandler> connections) {
        return Lists.convert(connections, connection -> connection.getConnectionId());
    }

    DatabaseCompatibility getCompatibility();

    String getDebuggerVersion();

    default void resetCompatibilityMonitor() {
    }

    boolean isCloudDatabase();

    @Nullable
    static ConnectionHandler get(ConnectionId connectionId) {
        if (connectionId == null) return null;

        ConnectionHandler connection = ConnectionCache.resolve(connectionId);
        if (connection != null) return connection;

        Project project = ThreadMonitor.getProject();
        if (isNotValid(project)) return null;

        ConnectionManager connectionManager = ConnectionManager.getInstance(project);
        return connectionManager.getConnection(connectionId);
    }

    @NotNull
    static ConnectionHandler ensure(ConnectionId connectionId) {
        return nd(get(connectionId));
    }

    @NotNull
    static ConnectionHandler local() {
        return ConnectionContext.local().getConnection();
    }

    static boolean canConnect(ConnectionHandler connection) {
        return connection != null && connection.canConnect() && connection.isValid();
    }

    default String getQualifiedName() {
        return getDatabaseType() + " connection " + getName();
    }

    default void updateLastAccess() {};
}
