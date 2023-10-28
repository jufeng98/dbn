package com.dci.intellij.dbn.common.ui;

import com.dci.intellij.dbn.common.event.ProjectEvents;
import com.dci.intellij.dbn.common.ref.WeakRef;
import com.dci.intellij.dbn.common.thread.Dispatch;
import com.dci.intellij.dbn.common.ui.panel.DBNPanelImpl;
import com.dci.intellij.dbn.common.ui.util.Fonts;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionRef;
import com.dci.intellij.dbn.connection.ConnectionStatusListener;
import com.dci.intellij.dbn.connection.SessionId;
import com.dci.intellij.dbn.connection.jdbc.DBNConnection;
import com.dci.intellij.dbn.connection.mapping.FileConnectionContextListener;
import com.dci.intellij.dbn.connection.session.DatabaseSession;
import com.dci.intellij.dbn.connection.transaction.TransactionAction;
import com.dci.intellij.dbn.connection.transaction.TransactionListener;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static com.dci.intellij.dbn.common.util.Commons.nvl;

public class AutoCommitLabel extends DBNPanelImpl implements Disposable {
    private interface Colors {
        Color DISCONNECTED = new JBColor(new Color(0x454545), new Color(0x808080));
        Color CONNECTED = new JBColor(new Color(0x454545), new Color(0x808080));
    }
    private ConnectionRef connection;
    private WeakRef<VirtualFile> virtualFile;
    private SessionId sessionId;
    private boolean subscribed = false;

    private final JLabel connectionLabel;
    private final JLabel autoCommitLabel;

    public AutoCommitLabel() {
        setLayout(new BorderLayout());
        connectionLabel = new JLabel();
        //connectionLabel.setFont(GUIUtil.BOLD_FONT);
        add(connectionLabel, BorderLayout.EAST);

        autoCommitLabel = new JLabel();
        autoCommitLabel.setFont(Fonts.BOLD);
        add(autoCommitLabel, BorderLayout.WEST);

        add(new JLabel(" "), BorderLayout.CENTER);

    }

    public void init(Project project, VirtualFile file, ConnectionHandler connection, DatabaseSession session) {
        init(project, file, connection, session == null ? null : session.getId());
    }

    public void init(Project project, VirtualFile file, ConnectionHandler connection, SessionId sessionId) {
        this.virtualFile = WeakRef.of(file);
        this.connection = ConnectionRef.of(connection);
        this.sessionId = nvl(sessionId, SessionId.MAIN);
        if (!subscribed) {
            subscribed = true;
            ProjectEvents.subscribe(project, this, ConnectionStatusListener.TOPIC, connectionStatusListener);
            ProjectEvents.subscribe(project, this, FileConnectionContextListener.TOPIC, connectionMappingListener);
            ProjectEvents.subscribe(project, this, TransactionListener.TOPIC, transactionListener);
        }
        update();
    }

    private void update() {
        Dispatch.run(true, () -> {
            ConnectionHandler connection = getConnection();
            if (connection != null && !connection.isVirtual()) {
                setVisible(true);
                boolean disconnected = !connection.isConnected(sessionId);
                boolean autoCommit = connection.isAutoCommit();

                connectionLabel.setForeground(disconnected ? Colors.DISCONNECTED : Colors.CONNECTED);
                DatabaseSession session = connection.getSessionBundle().getSession(sessionId);


                String sessionName = session.getName();
                connectionLabel.setText(disconnected ? " - not connected" : " - connected");
                connectionLabel.setToolTipText(
                        disconnected ?
                                "Not connected to " + sessionName + " database session" : "");

                connectionLabel.setFont(disconnected ? Fonts.REGULAR : Fonts.BOLD);

                autoCommitLabel.setForeground(autoCommit ?
                        com.dci.intellij.dbn.common.color.Colors.FAILURE_COLOR :
                        com.dci.intellij.dbn.common.color.Colors.SUCCESS_COLOR);
                autoCommitLabel.setText(autoCommit ? "Auto-Commit ON" : "Auto-Commit OFF");
                autoCommitLabel.setToolTipText(
                        autoCommit ?
                                "Auto-Commit is enabled for connection \"" + connection + "\". Data changes will be automatically committed to the database." :
                                "Auto-Commit is disabled for connection \"" + connection + "\". Data changes will need to be manually committed to the database.");
            } else {
                setVisible(false);
            }
        });
    }

    @Nullable
    private ConnectionHandler getConnection() {
        return ConnectionRef.get(connection);
    }

    private final ConnectionStatusListener connectionStatusListener = (connectionId, sessionId) -> {
        ConnectionHandler connection = getConnection();
        if (connection != null && connection.getConnectionId() == connectionId) {
            update();
        }
    };

    private final FileConnectionContextListener connectionMappingListener = new FileConnectionContextListener() {
        @Override
        public void connectionChanged(Project project, VirtualFile file, ConnectionHandler connection) {
            VirtualFile localVirtualFile = getVirtualFile();
            if (file.equals(localVirtualFile)) {
                AutoCommitLabel.this.connection = ConnectionRef.of(connection);
                update();
            }
        }

        @Override
        public void sessionChanged(Project project, VirtualFile file, DatabaseSession session) {
            VirtualFile localVirtualFile = getVirtualFile();
            if (file.equals(localVirtualFile)) {
                sessionId = session == null ? SessionId.MAIN : session.getId();
                update();
            }
        }
    };

    /********************************************************
     *                Transaction Listener                  *
     ********************************************************/
    private final TransactionListener transactionListener = new TransactionListener() {
        @Override
        public void afterAction(@NotNull ConnectionHandler connection, DBNConnection conn, TransactionAction action, boolean succeeded) {
            if (action.isOneOf(
                    TransactionAction.TURN_AUTO_COMMIT_ON,
                    TransactionAction.TURN_AUTO_COMMIT_OFF) &&
                    ConnectionRef.get(AutoCommitLabel.this.connection) == connection) {

                update();
            }
        }
    };


    @Nullable
    public VirtualFile getVirtualFile() {
        return WeakRef.get(virtualFile);
    }
}
