package com.dci.intellij.dbn.database.common.statement;

import com.dci.intellij.dbn.common.util.Commons;
import com.dci.intellij.dbn.common.util.Compactables;
import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.connection.Resources;
import com.dci.intellij.dbn.connection.jdbc.DBNConnection;
import com.dci.intellij.dbn.connection.jdbc.DBNPreparedStatement;
import com.dci.intellij.dbn.connection.jdbc.DBNStatement;
import com.dci.intellij.dbn.database.DatabaseActivityTrace;
import com.dci.intellij.dbn.database.DatabaseCompatibility;
import com.dci.intellij.dbn.database.DatabaseInterface;
import com.dci.intellij.dbn.database.DatabaseInterfaceProvider;
import com.dci.intellij.dbn.database.common.statement.StatementExecutor.Context;
import com.dci.intellij.dbn.diagnostics.DiagnosticsManager;
import com.dci.intellij.dbn.diagnostics.data.DiagnosticBundle;
import lombok.extern.slf4j.Slf4j;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static com.dci.intellij.dbn.common.options.setting.SettingsSupport.booleanAttribute;
import static com.dci.intellij.dbn.common.options.setting.SettingsSupport.stringAttribute;
import static com.dci.intellij.dbn.diagnostics.Diagnostics.isDatabaseAccessDebug;

@Slf4j
public class StatementExecutionProcessor {
    public static final SQLFeatureNotSupportedException NO_STATEMENT_DEFINITION_EXCEPTION = new SQLFeatureNotSupportedException("No statement definition found");

    private final DatabaseInterfaceProvider interfaceProvider;
    private final String id;
    private final boolean query;
    private final boolean prepared;
    private int timeout = 30;
    private List<StatementDefinition> statementDefinitions = new ArrayList<>();


    public StatementExecutionProcessor(Element element, DatabaseInterfaceProvider interfaceProvider) {
        this.interfaceProvider = interfaceProvider;
        this.id = stringAttribute(element, "id");
        this.query = booleanAttribute(element, "is-query", false);
        this.prepared = booleanAttribute(element, "is-prepared-statement", false);
        String customTimeout = element.getAttributeValue("timeout");
        if (Strings.isNotEmpty(customTimeout)) {
            timeout = Integer.parseInt(customTimeout);
        }

        List<Element> children = element.getChildren();
        if (children.isEmpty()) {
            String statementText = element.getTextTrim();
            readStatements(statementText, null);
        } else {
            for (Element child : children) {
                String statementText = child.getTextTrim();
                String prefixes = child.getAttributeValue("prefixes");
                readStatements(statementText, prefixes);
            }
        }
        statementDefinitions = Compactables.compact(statementDefinitions);
    }

    private void readStatements(String statementText, String prefixes) {
        if (prefixes == null) {
            StatementDefinition statementDefinition = new StatementDefinition(statementText, null, prepared, false);
            statementDefinitions.add(statementDefinition);
        } else {
            StringTokenizer tokenizer = new StringTokenizer(prefixes, ",");
            while (tokenizer.hasMoreTokens()) {
                String prefix = tokenizer.nextToken().trim();
                boolean hasFallback = tokenizer.hasMoreTokens();
                StatementDefinition statementDefinition = new StatementDefinition(statementText, prefix, prepared, hasFallback);
                statementDefinitions.add(statementDefinition);
            }
        }
    }

    public DatabaseInterfaceProvider getInterfaceProvider() {
        return interfaceProvider;
    }

    public String getId() {
        return id;
    }

    public ResultSet executeQuery(@NotNull DBNConnection connection, boolean forceExecution, Object... arguments) throws SQLException {
        if (statementDefinitions.size() == 1) {
            return executeQuery(statementDefinitions.get(0), connection, forceExecution, arguments);
        } else {
            SQLException exception = NO_STATEMENT_DEFINITION_EXCEPTION;
            for (StatementDefinition statementDefinition : statementDefinitions) {
                try {
                    return executeQuery(statementDefinition, connection, forceExecution, arguments);
                } catch (SQLException e){
                    exception = e;
                }
            }
            throw exception;
        }
    }

    private ResultSet executeQuery(
            @NotNull StatementDefinition statementDefinition,
            @NotNull DBNConnection connection,
            boolean forceExecution,
            Object... arguments) throws SQLException {

        boolean hasFallback = statementDefinition.hasFallback();
        DatabaseCompatibility compatibility = DatabaseInterface.getConnection().getCompatibility();
        DatabaseActivityTrace activityTrace = compatibility.getActivityTrace(statementDefinition.getId());

        if (forceExecution || activityTrace.canExecute(hasFallback)) {
            Context context = createExecutionContext(connection);
            return StatementExecutor.execute(
                    context,
                    () -> {
                        DBNStatement statement = null;
                        ResultSet resultSet = null;
                        String statementText = null;
                        try {
                            activityTrace.init();
                            if (isDatabaseAccessDebug()) {
                                statementText = statementDefinition.prepareStatementText(arguments);
                                log.info("[DBN] Executing statement: " + statementText);
                            }
                            if (prepared) {
                                DBNPreparedStatement preparedStatement = statementDefinition.prepareStatement(connection, arguments);
                                statement = preparedStatement;
                                context.setStatement(statement);
                                preparedStatement.setQueryTimeout(timeout);
                                resultSet = preparedStatement.executeQuery();
                                return resultSet;
                            } else {
                                if (statementText == null)
                                    statementText = statementDefinition.prepareStatementText(arguments);
                                statement = connection.createStatement();
                                context.setStatement(statement);
                                statement.setQueryTimeout(timeout);
                                statement.execute(statementText);
                                if (query) {
                                    try {
                                        resultSet = statement.getResultSet();
                                        return resultSet;
                                    } catch (SQLException e) {
                                        Resources.close(statement);
                                        return null;
                                    }
                                } else {
                                    Resources.close(statement);
                                    return null;
                                }
                            }
                        } catch (SQLException exception) {
                            Resources.close(statement);
                            String message = exception.getMessage();
                            if (isDatabaseAccessDebug()) log.warn("[DBN] Error executing statement: " + statementText + "\nCause: " + message);

                            boolean isModelException = interfaceProvider.getMessageParserInterface().isModelException(exception);
                            SQLException traceException =
                                    isModelException ?
                                            new SQLException("Model exception received while executing query '" + id +"'. " + message) :
                                            new SQLException("Too many failed attempts of executing query '" + id +"'. " + message);

                            activityTrace.fail(traceException, isModelException);
                            throw exception;
                        } finally {
                            activityTrace.release();
                            if (resultSet == null && statement != null) {
                                if (statement.isCached()) {
                                    statement.park();
                                } else {
                                    Resources.close(statement);
                                }

                            }
                        }
                    });
        } else {
            throw Commons.nvl(
                    activityTrace.getException(),
                    () -> new SQLException("Too many failed attempts of executing query '" + id + "'."));
        }
    }

    public <T extends CallableStatementOutput> T executeCall(
            @NotNull DBNConnection connection,
            @Nullable T outputReader,
            Object... arguments) throws SQLException {

        if (statementDefinitions.size() == 1) {
            return executeCall(statementDefinitions.get(0), connection, outputReader, arguments);
        } else {
            SQLException exception = NO_STATEMENT_DEFINITION_EXCEPTION;
            for (StatementDefinition statementDefinition : statementDefinitions) {
                try {
                    return executeCall(statementDefinition, connection, outputReader, arguments);
                } catch (SQLException e){
                    exception = e;
                }

            }
            throw exception;
        }
    }

    private <T extends CallableStatementOutput> T executeCall(
            @NotNull StatementDefinition statementDefinition,
            @NotNull DBNConnection connection,
            @Nullable T outputReader,
            Object... arguments) throws SQLException {

        Context context = createExecutionContext(connection);
        return StatementExecutor.execute(
                context,
                () -> {
                    String statementText = statementDefinition.prepareStatementText(arguments);
                    if (isDatabaseAccessDebug()) log.info("[DBN] Executing statement: " + statementText);

                    CallableStatement statement = connection.prepareCall(statementText);
                    context.setStatement(statement);
                    try {
                        if (outputReader != null) outputReader.registerParameters(statement);
                        statement.setQueryTimeout(timeout);
                        statement.execute();
                        if (outputReader != null) outputReader.read(statement);
                        return outputReader;
                    } catch (SQLException exception) {
                        if (isDatabaseAccessDebug())
                            log.warn("[DBN] Error executing statement: " + statementText + "\nCause: " + exception.getMessage());

                        throw exception;
                    } finally {
                        Resources.close(statement);
                    }
                });
    }

    public void executeUpdate(DBNConnection connection, Object... arguments) throws SQLException {
        if (statementDefinitions.size() == 1) {
            executeUpdate(statementDefinitions.get(0), connection, arguments);
        } else {
            SQLException exception = NO_STATEMENT_DEFINITION_EXCEPTION;
            for (StatementDefinition statementDefinition : statementDefinitions) {
                try {
                    executeUpdate(statementDefinition, connection, arguments);
                    return;
                } catch (SQLException e){
                    exception = e;
                }
            }
            throw exception;
        }
    }

    private void executeUpdate(
            @NotNull StatementDefinition statementDefinition,
            @NotNull DBNConnection connection,
            Object... arguments) throws SQLException {
        Context context = createExecutionContext(connection);
        StatementExecutor.execute(
                context,
                () -> {
                    String statementText = statementDefinition.prepareStatementText(arguments);
                    if (isDatabaseAccessDebug()) log.info("[DBN] Executing statement: " + statementText);

                    Statement statement = connection.createStatement();
                    context.setStatement(statement);
                    try {
                        statement.setQueryTimeout(timeout);
                        statement.executeUpdate(statementText);
                    } catch (SQLException exception) {
                        if (isDatabaseAccessDebug())
                            log.warn("[DBN] Error executing statement: " + statementText + "\nCause: " + exception.getMessage());

                        throw exception;
                    } finally {
                        Resources.close(statement);
                    }
                    return null;
                });
    }

    public boolean executeStatement(@NotNull DBNConnection connection, Object... arguments) throws SQLException {
        if (statementDefinitions.size() == 1) {
            return executeStatement(statementDefinitions.get(0), connection, arguments);
        } else {
            SQLException exception = NO_STATEMENT_DEFINITION_EXCEPTION;
            for (StatementDefinition statementDefinition : statementDefinitions) {
                try {
                    return executeStatement(statementDefinition, connection, arguments);
                } catch (SQLException e){
                    exception = e;
                }
            }
            throw exception;
        }
    }

    private boolean executeStatement(
            @NotNull StatementDefinition statementDefinition,
            @NotNull DBNConnection connection,
            Object... arguments) throws SQLException {

        Context context = createExecutionContext(connection);
        return StatementExecutor.execute(context,
                () -> {
                    String statementText = statementDefinition.prepareStatementText(arguments);
                    if (isDatabaseAccessDebug()) log.info("[DBN] Executing statement: " + statementText);

                    Statement statement = connection.createStatement();
                    context.setStatement(statement);
                    try {
                        statement.setQueryTimeout(timeout);
                        return statement.execute(statementText);
                    } catch (SQLException exception) {
                        if (isDatabaseAccessDebug())
                            log.warn("[DBN] Error executing statement: " + statementText + "\nCause: " + exception.getMessage());

                        throw exception;
                    } finally {
                        Resources.close(statement);
                    }
                });
    }

    @NotNull
    private Context createExecutionContext(@NotNull DBNConnection connection) {
        DiagnosticsManager diagnosticsManager = DiagnosticsManager.getInstance(connection.getProject());
        DiagnosticBundle<String> diagnostics =  diagnosticsManager.getMetadataInterfaceDiagnostics(connection.getId());
        return StatementExecutor.context(diagnostics, id, timeout);
    }
}