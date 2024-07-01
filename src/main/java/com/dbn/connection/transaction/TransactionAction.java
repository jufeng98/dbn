package com.dbn.connection.transaction;

import com.dbn.common.constant.Constant;
import com.dbn.common.notification.NotificationGroup;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.Resources;
import com.dbn.connection.jdbc.DBNConnection;
import com.intellij.notification.NotificationType;
import lombok.Getter;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.dbn.nls.NlsResources.nls;

@Getter
public enum TransactionAction implements Serializable, Constant<TransactionAction> {
    COMMIT(
            nls("app.transactions.action.Commit"),
            NotificationGroup.TRANSACTION,
            NotificationType.INFORMATION, "msg.transactions.confirmation.Commit",
            NotificationType.ERROR, "msg.transactions.error.Commit",
            false,
            (connection, target) -> Resources.commit(target)),

    ROLLBACK(
            nls("app.transactions.action.Rollback"),
            NotificationGroup.TRANSACTION,
            NotificationType.INFORMATION, "msg.transactions.confirmation.Rollback",
            NotificationType.ERROR, "msg.transactions.error.Rollback",
            false,
            (connection, target) -> Resources.rollback(target)),

    ROLLBACK_IDLE(
            nls("app.transactions.action.IdleRollback"),
            NotificationGroup.TRANSACTION,
            NotificationType.INFORMATION, "msg.transactions.confirmation.Rollback",
            NotificationType.ERROR, "msg.transactions.error.Rollback",
            false,
            (connection, target) -> Resources.rollback(target)),

    DISCONNECT(
            nls("app.transactions.action.Disconnect"),
            NotificationGroup.SESSION,
            NotificationType.INFORMATION, "msg.transactions.confirmation.Disconnect",
            NotificationType.WARNING, "msg.transactions.error.Disconnect",
            true,
            (connection, target) -> connection.closeConnection(target)),

    DISCONNECT_IDLE(
            nls("app.transactions.action.IdleDisconnect"),
            NotificationGroup.SESSION,
            NotificationType.INFORMATION, "msg.transactions.confirmation.IdleDisconnect",
            NotificationType.WARNING, "msg.transactions.error.Disconnect",
            true,
            (connection, target) -> connection.closeConnection(target)),

    KEEP_ALIVE(
            nls("app.transactions.action.KeepAlive"),
            NotificationGroup.CONNECTION,
            null, "",
            NotificationType.ERROR, "msg.transactions.error.KeepAlive",
            false,
            (connection, target) -> target.updateLastAccess()),

    TURN_AUTO_COMMIT_ON(
            nls("app.transactions.action.EnableAutoCommit"),
            NotificationGroup.TRANSACTION,
            NotificationType.WARNING, "msg.transactions.confirmation.EnableAutoCommit",
            NotificationType.ERROR, "msg.transactions.error.EnableAutoCommit",
            true,
            (connection, target) -> target.setAutoCommit(true)),

    TURN_AUTO_COMMIT_OFF(
            nls("app.transactions.action.DisableAutoCommit"),
            NotificationGroup.TRANSACTION,
            NotificationType.INFORMATION, "msg.transactions.confirmation.DisableAutoCommit",
            NotificationType.ERROR, "msg.transactions.error.DisableAutoCommit",
            true,
            (connection, target) -> target.setAutoCommit(false));


    private final NotificationGroup group;
    private final String name;
    private final String successNotificationMessage;
    private final String failureNotificationMessage;
    private final NotificationType notificationType;
    private final NotificationType failureNotificationType;
    private final Executor executor;
    private final boolean statusChange;

    TransactionAction(
            String name,
            NotificationGroup group,
            NotificationType notificationType,
            @Nls String successNotificationMessage,
            NotificationType failureNotificationType,
            @Nls String failureNotificationMessage,
            boolean statusChange,
            Executor executor) {
        this.group = group;
        this.name = name;
        this.failureNotificationMessage = failureNotificationMessage;
        this.successNotificationMessage = successNotificationMessage;
        this.executor = executor;
        this.statusChange = statusChange;
        this.notificationType = notificationType;
        this.failureNotificationType = failureNotificationType;
    }

    @FunctionalInterface
    private interface Executor {
        void execute(@NotNull ConnectionHandler connection, @NotNull DBNConnection target) throws SQLException;
    }

    public void execute(@NotNull ConnectionHandler connection, @NotNull DBNConnection target) throws SQLException {
        executor.execute(connection, target);
    }

    public static List<TransactionAction> actions(TransactionAction ... actions) {
        return Arrays.stream(actions).filter(action -> action != null).collect(Collectors.toList());
    }

}
