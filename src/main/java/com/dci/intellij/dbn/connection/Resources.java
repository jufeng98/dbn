package com.dci.intellij.dbn.connection;

import com.dci.intellij.dbn.common.notification.NotificationGroup;
import com.dci.intellij.dbn.common.notification.NotificationSupport;
import com.dci.intellij.dbn.common.routine.ThrowableRunnable;
import com.dci.intellij.dbn.common.util.UUIDs;
import com.dci.intellij.dbn.common.util.Unsafe;
import com.dci.intellij.dbn.connection.jdbc.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.sql.Savepoint;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static com.dci.intellij.dbn.common.util.Commons.nvl;
import static com.dci.intellij.dbn.connection.jdbc.ResourceStatus.*;
import static com.dci.intellij.dbn.database.DatabaseFeature.READONLY_CONNECTIVITY;
import static com.dci.intellij.dbn.diagnostics.Diagnostics.conditionallyLog;
import static com.dci.intellij.dbn.diagnostics.Diagnostics.isDatabaseResourceDebug;

@Slf4j
@UtilityClass
public final class Resources {

    public static boolean isClosed(ResultSet resultSet) throws SQLException {
        try {
            return resultSet.isClosed();
        } catch (AbstractMethodError e) {
            conditionallyLog(e);
            // sqlite AbstractMethodError for osx
            return false;
        }
    }

    public static void markClosed(DBNConnection connection) {
        if (connection == null) return;

        connection.set(VALID, false);
        connection.set(ACTIVE, false);
        connection.set(CLOSED, true);
    }

    public static void cancel(DBNStatement statement) {
        try {
            if (statement == null || statement.isClosed()) return;
            invokeResourceAction(
                    statement,
                    ResourceStatus.CANCELLING,
                    () -> statement.cancel(),
                    () -> "[DBN] Cancelling " + statement,
                    () -> "[DBN] Done cancelling " + statement,
                    () -> "[DBN] Failed to cancel " + statement);
        } catch (Throwable e) {
            conditionallyLog(e);
        } finally {
            close((DBNResource) statement);
        }
    }

    public static <T extends AutoCloseable> void close(T resource) {
        if (resource == null) return;
        if (resource instanceof DBNResource) {
            close((DBNResource) resource);
        } else {
            try {
                invokeResourceAction(
                        () -> resource.close(),
                        () -> "[DBN] Closing " + resource,
                        () -> "[DBN] Done closing " + resource,
                        () -> "[DBN] Failed to close " + resource);
            } catch (Throwable e) {
                conditionallyLog(e);
            }
        }
    }

    private static <T extends DBNResource<?>> void close(T resource) {
        if (resource == null || resource.isClosed()) return;
        try {
            resource.getListeners().notify(l -> l.closing());
            invokeResourceAction(
                    resource,
                    ResourceStatus.CLOSING,
                    () -> resource.close(),
                    () -> "[DBN] Closing " + resource,
                    () -> "[DBN] Done closing " + resource,
                    () -> "[DBN] Failed to close " + resource);
        } catch (Throwable e) {
            conditionallyLog(e);
        }
        finally {
            resource.getListeners().notify(l -> l.closed());
        }
    }

    public static <T extends AutoCloseable> void close(Collection<T> resources) {
        for (T resource : resources) {
            close(resource);
        }
    }

    public static void commitSilently(DBNConnection connection) {
        Unsafe.silent(connection, c -> commit(c));
    }

    public static void commit(DBNConnection connection) throws SQLException {
        try {
            if (connection == null || connection.isAutoCommit()) return;
            invokeResourceAction(
                    connection,
                    ResourceStatus.COMMITTING,
                    () -> connection.commit(),
                    () -> "[DBN] Committing " + connection,
                    () -> "[DBN] Done committing " + connection,
                    () -> "[DBN] Failed to commit " + connection);
        } catch (SQLRecoverableException e) {
            conditionallyLog(e);
            markClosed(connection);
        } catch (SQLException e) {
            conditionallyLog(e);
            sentWarningNotification(
                    NotificationGroup.TRANSACTION,
                    "Failed to commit",
                    connection,
                    e);
            throw e;
        }
    }

    public static void rollbackSilently(DBNConnection connection) {
        Unsafe.silent(connection, c -> rollback(c));
    }

    public static void rollback(DBNConnection connection) throws SQLException {
        try {
            if (connection == null || connection.isAutoCommit()) return;
            invokeResourceAction(
                    connection,
                    ResourceStatus.ROLLING_BACK,
                    () -> connection.rollback(),
                    () -> "[DBN] Rolling-back " + connection,
                    () -> "[DBN] Done rolling-back " + connection,
                    () -> "[DBN] Failed to roll-back " + connection);
        } catch (SQLRecoverableException e) {
            conditionallyLog(e);
            markClosed(connection);
        } catch (SQLException e) {
            conditionallyLog(e);
            sentWarningNotification(
                    NotificationGroup.TRANSACTION,
                    "Failed to rollback",
                    connection,
                    e);
            throw e;
        }
    }

    public static void rollbackSilently(DBNConnection connection, @Nullable Savepoint savepoint) {
        Unsafe.silent(() -> rollback(connection, savepoint));
    }

    public static void rollback(DBNConnection connection, @Nullable Savepoint savepoint) throws SQLException {
        try {
            if (savepoint == null || isObsolete(connection) || connection.isAutoCommit()) return;
            String savepointId = getSavepointIdentifier(savepoint);
            invokeResourceAction(
                    connection,
                    ResourceStatus.ROLLING_BACK_SAVEPOINT,
                    () -> connection.rollback(savepoint),
                    () -> "[DBN] Rolling-back savepoint '" + savepointId + "' on " + connection,
                    () -> "[DBN] Done rolling-back savepoint '" + savepointId + "' on " + connection,
                    () -> "[DBN] Failed to roll-back savepoint '" + savepointId + "' on " + connection);
        } catch (SQLRecoverableException e) {
            conditionallyLog(e);
            markClosed(connection);
        } catch (SQLException e) {
            conditionallyLog(e);
            sentWarningNotification(
                    NotificationGroup.TRANSACTION,
                    "Failed to rollback savepoint for",
                    connection,
                    e);
            throw e;
        }
    }

    @Nullable
    public static Savepoint createSavepoint(DBNConnection connection) {
        try {
            if (isObsolete(connection) || connection.isAutoCommit()) return null;
            AtomicReference<Savepoint> savepoint = new AtomicReference<>();
            invokeResourceAction(
                    connection,
                    ResourceStatus.CREATING_SAVEPOINT,
                    () -> savepoint.set(connection.setSavepoint(UUIDs.compact())),
                    () -> "[DBN] Creating savepoint on " + connection,
                    () -> "[DBN] Done creating savepoint '" + getSavepointIdentifier(savepoint.get()) + "' on " + connection,
                    () -> "[DBN] Failed to create savepoint on " + connection);
            return savepoint.get();
        } catch (SQLRecoverableException e) {
            conditionallyLog(e);
            markClosed(connection);
        } catch (SQLException e) {
            conditionallyLog(e);
            sentWarningNotification(
                    NotificationGroup.TRANSACTION,
                    "Failed to create savepoint for",
                    connection,
                    e);
        }
        return null;
    }

    public static void releaseSavepoint(DBNConnection connection, @Nullable Savepoint savepoint) {
        try {
            if (savepoint == null || isObsolete(connection) || connection.isAutoCommit()) return;
            String savepointId = getSavepointIdentifier(savepoint);
            invokeResourceAction(
                    connection,
                    ResourceStatus.RELEASING_SAVEPOINT,
                    () -> connection.releaseSavepoint(savepoint),
                    () -> "[DBN] Releasing savepoint '" + savepointId + "' on " + connection,
                    () -> "[DBN] Done releasing savepoint '" + savepointId + "' on " + connection,
                    () -> "[DBN] Failed to release savepoint '" + savepointId + "' on " + connection);
        } catch (SQLRecoverableException e) {
            conditionallyLog(e);
            markClosed(connection);
        } catch (SQLException e) {
            conditionallyLog(e);
            sentWarningNotification(
                    NotificationGroup.TRANSACTION,
                    "Failed to release savepoint for",
                    connection,
                    e);
        }
    }

    public static void setReadonly(DBNConnection connection, boolean readonly) {
        if (READONLY_CONNECTIVITY.isNotSupported(connection.getConnectionHandler())) return;

        try {
            invokeResourceAction(
                    connection,
                    ResourceStatus.CHANGING_READ_ONLY,
                    () -> connection.setReadOnly(readonly),
                    () -> "[DBN] Applying status READ_ONLY=" + readonly + " on " + connection,
                    () -> "[DBN] Done applying status READ_ONLY=" + readonly + " on " + connection,
                    () -> "[DBN] Failed to apply status READ_ONLY=" + readonly + " on " + connection);
        } catch (SQLRecoverableException e) {
            conditionallyLog(e);
            markClosed(connection);
        } catch (SQLException e) {
            conditionallyLog(e);
            sentWarningNotification(
                    NotificationGroup.CONNECTION,
                    "Failed to initialize readonly status for",
                    connection,
                    e);
        }
    }

    public static void setAutoCommit(DBNConnection connection, boolean autoCommit) {
        try {
            if (isObsolete(connection)) return;
            invokeResourceAction(
                    connection,
                    ResourceStatus.CHANGING_AUTO_COMMIT, () -> connection.setAutoCommit(autoCommit),
                    () -> "[DBN] Applying status AUTO_COMMIT=" + autoCommit + " on " + connection,
                    () -> "[DBN] Done applying status AUTO_COMMIT=" + autoCommit + " on " + connection,
                    () -> "[DBN] Failed to apply status AUTO_COMMIT=" + autoCommit + " on " + connection);

            connection.setAutoCommit(autoCommit);
        } catch (SQLRecoverableException e) {
            conditionallyLog(e);
            markClosed(connection);
        } catch (Exception e) {
            conditionallyLog(e);
            sentWarningNotification(
                    NotificationGroup.CONNECTION,
                    "Failed to change auto-commit status for",
                    connection,
                    e);
        }
    }

    private static void sentWarningNotification(NotificationGroup title, String message, DBNConnection connection, Exception e) {
        String error = nvl(e.getMessage(), e.getClass().getName());
        if (connection.shouldNotify(error)) {
            String name = connection.getName();
            SessionId sessionId = connection.getSessionId();
            String errorMessage = e.getMessage();
            String notificationMessage = message + " connection \"" + name + " (" + sessionId + ")\": " + errorMessage;

            NotificationSupport.sendWarningNotification(
                    connection.getProject(),
                    title,
                    notificationMessage);
        }
    }

    private static <E extends Throwable> void invokeResourceAction(
            @NotNull DBNResource<?> resource,
            @NotNull ResourceStatus transientStatus,
            @NotNull ThrowableRunnable<E> action,
            @NotNull Supplier<String> startMessage,
            @NotNull Supplier<String> successMessage,
            @NotNull Supplier<String> errorMessage) throws E{

        if (resource.is(transientStatus)) return;

        try {
            resource.set(transientStatus, true);
            invokeResourceAction(action, startMessage, successMessage, errorMessage);
        } finally {
            resource.set(transientStatus, false);
        }
    }

    private static <E extends Throwable> void invokeResourceAction(
            @NotNull ThrowableRunnable<E> action,
            @NotNull Supplier<String> startMessage,
            @NotNull Supplier<String> successMessage,
            @NotNull Supplier<String> errorMessage) throws E{

        long start = System.currentTimeMillis();
        if (isDatabaseResourceDebug()) log.info("{}...", startMessage.get());
        try {
            action.run();
            if (isDatabaseResourceDebug()) log.info("{} - {}ms", successMessage.get(), System.currentTimeMillis() - start);
        } catch (Throwable e) {
            conditionallyLog(e);
            log.warn("{} Cause: {}", errorMessage.get(),  e.getMessage());
            throw e;
        }
    }

    public static String getSavepointIdentifier(Savepoint savepoint) {
        try {
            return savepoint.getSavepointName();
        } catch (SQLException e) {
            conditionallyLog(e);
            try {
                return Integer.toString(savepoint.getSavepointId());
            } catch (SQLException ex) {
                conditionallyLog(ex);
                return "UNKNOWN";
            }
        }
    }

    public static boolean isObsolete(@Nullable Resource resource) {
        return resource == null || resource.isObsolete();
    }
}
