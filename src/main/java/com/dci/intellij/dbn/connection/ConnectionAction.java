package com.dci.intellij.dbn.connection;

import com.dci.intellij.dbn.common.database.AuthenticationInfo;
import com.dci.intellij.dbn.common.load.ProgressMonitor;
import com.dci.intellij.dbn.common.routine.Consumer;
import com.dci.intellij.dbn.common.util.Commons;
import com.dci.intellij.dbn.connection.context.DatabaseContext;
import com.dci.intellij.dbn.connection.context.DatabaseContextBase;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

import static com.dci.intellij.dbn.common.dispose.Failsafe.guarded;
import static com.dci.intellij.dbn.common.dispose.Failsafe.nd;

public abstract class ConnectionAction implements DatabaseContextBase{
    static final String[] OPTIONS_CONNECT_CANCEL = Commons.list("Connect", "Cancel");

    private final String description;
    private final boolean interactive;
    private final DatabaseContext context;
    private boolean cancelled;

    private ConnectionAction(String description, boolean interactive, DatabaseContext context) {
        this.description = description;
        this.interactive = interactive;
        this.context = context;
    }

    @NotNull
    public Project getProject() {
        return getConnection().getProject();
    }

    public boolean isCancelled() {
        if (cancelled) {
            return true;
        } else {
            return ProgressMonitor.isProgressCancelled();
        }
    }

    protected abstract void execute();

    protected void cancel() {
        cancelled = true;
    }

    public final void start() {
        ConnectionHandler connection = getConnection();
        if (connection.isVirtual() || connection.canConnect()) {
            if (interactive || connection.isValid()) {
                guarded(this, a -> a.execute());
            } else {
                String connectionName = connection.getName();
                Throwable connectionException = connection.getConnectionStatus().getConnectionException();
                ConnectionManager.showErrorConnectionMessage(getProject(), connectionName, connectionException);
            }
        } else {
            if (connection.isDatabaseInitialized()) {
                if (connection.isAuthenticationProvided()) {
                    promptConnectDialog();
                } else {
                    promptAuthenticationDialog();
                }
            } else {
                promptDatabaseInitDialog();
            }
        }
    }

    private void promptDatabaseInitDialog() {
        ConnectionHandler connection = getConnection();
        ConnectionManager.promptDatabaseInitDialog(
                connection,
                option -> {
                    if (option == 0) {
                        ConnectionInstructions instructions = connection.getInstructions();
                        instructions.setAllowAutoInit(true);
                        instructions.setAllowAutoConnect(true);
                        if (connection.isAuthenticationProvided()) {
                            guarded(this, r -> r.execute());
                        } else {
                            promptAuthenticationDialog();
                        }
                    } else {
                        cancel();
                    }
                });
    }

    private void promptAuthenticationDialog() {
        ConnectionHandler connection = getConnection();
        AuthenticationInfo temporaryAuthenticationInfo = connection.getAuthenticationInfo().clone();
        temporaryAuthenticationInfo.setTemporary(true);
        ConnectionManager connectionManager = ConnectionManager.getInstance(connection.getProject());
        connectionManager.promptAuthenticationDialog(
                connection,
                temporaryAuthenticationInfo,
                authenticationInfo -> {
                    if (authenticationInfo != null) {
                        guarded(() -> execute());
                    } else {
                        cancel();
                    }
                });
    }

    private void promptConnectDialog() {
        ConnectionHandler connection = getConnection();
        ConnectionManager.promptConnectDialog(
                connection,
                description,
                option -> {
                    if (option == 0) {
                        connection.getInstructions().setAllowAutoConnect(true);
                        guarded(() -> execute());
                    } else {
                        cancel();
                    }
                });
    }

    @NotNull
    public ConnectionHandler getConnection() {
        return nd(this.context).ensureConnection();
    }

    public static void invoke(
            String description,
            boolean interactive,
            DatabaseContext databaseContext,
            Consumer<ConnectionAction> action) {
        new ConnectionAction(description, interactive, databaseContext) {
            @Override
            public void execute() {
                guarded(() -> action.accept(this));
            }
        }.start();
    }

    public static void invoke(
            String description,
            boolean interactive,
            DatabaseContext databaseContext,
            Consumer<ConnectionAction> action,
            Consumer<ConnectionAction> cancel,
            Predicate<ConnectionAction> canExecute) {

        new ConnectionAction(description, interactive, databaseContext) {
            @Override
            public void execute() {
                if (canExecute == null || canExecute.test(this)) {
                    guarded(() -> action.accept(this));
                }
            }

            @Override
            protected void cancel() {
                super.cancel();
                if (cancel != null){
                    cancel.accept(this);
                }
            }

        }.start();
    }


}
