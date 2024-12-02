package com.dbn.connection.config.ui;

import com.dbn.common.color.Colors;
import com.dbn.common.environment.EnvironmentType;
import com.dbn.common.event.ProjectEvents;
import com.dbn.common.icon.Icons;
import com.dbn.common.options.ConfigurationHandle;
import com.dbn.common.options.SettingsChangeNotifier;
import com.dbn.common.options.ui.CompositeConfigurationEditorForm;
import com.dbn.common.thread.Dispatch;
import com.dbn.common.ui.form.DBNHeaderForm;
import com.dbn.common.ui.util.UserInterface;
import com.dbn.common.util.Messages;
import com.dbn.common.util.Safe;
import com.dbn.connection.ConnectionManager;
import com.dbn.connection.ConnectivityStatus;
import com.dbn.connection.DatabaseType;
import com.dbn.connection.config.ConnectionBundleSettings;
import com.dbn.connection.config.ConnectionConfigListener;
import com.dbn.connection.config.ConnectionConfigType;
import com.dbn.connection.config.ConnectionDatabaseSettings;
import com.dbn.connection.config.ConnectionDebuggerSettings;
import com.dbn.connection.config.ConnectionDetailSettings;
import com.dbn.connection.config.ConnectionFilterSettings;
import com.dbn.connection.config.ConnectionPropertiesSettings;
import com.dbn.connection.config.ConnectionSettings;
import com.dbn.connection.config.ConnectionSshTunnelSettings;
import com.dbn.connection.config.ConnectionSslSettings;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.tabs.JBTabs;
import com.intellij.ui.tabs.JBTabsFactory;
import com.intellij.ui.tabs.TabInfo;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import static com.dbn.common.dispose.Checks.isNotValid;
import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

public class ConnectionSettingsForm extends CompositeConfigurationEditorForm<ConnectionSettings> {
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JPanel headerPanel;
    private JButton infoButton;
    private JButton testButton;
    private JCheckBox activeCheckBox;

    private final JBTabs tabbedPane;
    private final DBNHeaderForm headerForm;

    public ConnectionSettingsForm(ConnectionSettings connectionSettings) {
        super(connectionSettings);
        ConnectionDatabaseSettings databaseSettings = connectionSettings.getDatabaseSettings();
        tabbedPane = JBTabsFactory.createTabs(getProject(), connectionSettings);
        contentPanel.add((Component) tabbedPane, BorderLayout.CENTER);

        TabInfo connectionTabInfo = new TabInfo(new JBScrollPane(databaseSettings.createComponent()));
        connectionTabInfo.setText(nls("cfg.connection.title.Database"));
        tabbedPane.addTab(connectionTabInfo);

        if (databaseSettings.getConfigType() == ConnectionConfigType.BASIC) {
            ConnectionSslSettings sslSettings = connectionSettings.getSslSettings();
            TabInfo sslTabInfo = new TabInfo(new JBScrollPane(sslSettings.createComponent()));
            sslTabInfo.setText(nls("cfg.connection.title.Ssl"));
            tabbedPane.addTab(sslTabInfo);

            ConnectionSshTunnelSettings sshTunnelSettings = connectionSettings.getSshTunnelSettings();
            TabInfo sshTunnelTabInfo = new TabInfo(new JBScrollPane(sshTunnelSettings.createComponent()));
            sshTunnelTabInfo.setText(nls("cfg.connection.title.SslTunnel"));
            tabbedPane.addTab(sshTunnelTabInfo);
        }

        ConnectionPropertiesSettings propertiesSettings = connectionSettings.getPropertiesSettings();
        TabInfo propertiesTabInfo = new TabInfo(new JBScrollPane(propertiesSettings.createComponent()));
        propertiesTabInfo.setText(nls("cfg.connection.title.Properties"));
        tabbedPane.addTab(propertiesTabInfo);

        ConnectionDetailSettings detailSettings = connectionSettings.getDetailSettings();
        TabInfo detailsTabInfo = new TabInfo(new JBScrollPane(detailSettings.createComponent()));
        detailsTabInfo.setText(nls("cfg.connection.title.Details"));
        tabbedPane.addTab(detailsTabInfo);

        if (databaseSettings.getDatabaseType() == DatabaseType.ORACLE) {
            ConnectionDebuggerSettings debuggerSettings = connectionSettings.getDebuggerSettings();
            TabInfo debuggerTabInfo = new TabInfo(new JBScrollPane(debuggerSettings.createComponent()));
            debuggerTabInfo.setText(nls("cfg.connection.title.Debugger"));
            tabbedPane.addTab(debuggerTabInfo);
        }

        ConnectionFilterSettings filterSettings = connectionSettings.getFilterSettings();
        TabInfo filtersTabInfo = new TabInfo(new JBScrollPane(filterSettings.createComponent()));
        filtersTabInfo.setText(nls("cfg.connection.title.Filters"));
        tabbedPane.addTab(filtersTabInfo);

        ConnectivityStatus connectivityStatus = databaseSettings.getConnectivityStatus();
        Icon icon = connectionSettings.isNew() ? Icons.CONNECTION_NEW :
                !connectionSettings.isActive() ? Icons.CONNECTION_DISABLED :
                        connectivityStatus == ConnectivityStatus.VALID ? Icons.CONNECTION_CONNECTED :
                                connectivityStatus == ConnectivityStatus.INVALID ? Icons.CONNECTION_INVALID : Icons.CONNECTION_INACTIVE;

        String name = connectionSettings.getDatabaseSettings().getName();
        Color color = detailSettings.getEnvironmentType().getColor();

        headerForm = new DBNHeaderForm(this, name, icon, color);
        headerPanel.add(headerForm.getComponent(), BorderLayout.CENTER);
        //if (databaseType != null) databaseIconLabel.setIcon(databaseType.getLargeIcon());
        ConnectionPresentationChangeListener connectionPresentationChangeListener = (name1, icon1, color1, connectionId, databaseType) ->
                Dispatch.run(() -> {
                    if (isNotValid(ConnectionSettingsForm.this)) return;

                    ConnectionSettings configuration = getConfiguration();
                    if (!configuration.getConnectionId().equals(connectionId)) return;

                    DBNHeaderForm header = headerForm;

                    if (name1 != null) header.setTitle(name1);
                    if (icon1 != null) header.setIcon(icon1);
                    if (color1 != null) header.setBackground(color1);
                    else header.setBackground(Colors.getPanelBackground());
                    //if (databaseType != null) databaseIconLabel.setIcon(databaseType.getLargeIcon());
                });
        ProjectEvents.subscribe(ensureProject(), this, ConnectionPresentationChangeListener.TOPIC, connectionPresentationChangeListener);

        //databaseSettingsForm.notifyPresentationChanges();
        //detailSettingsForm.notifyPresentationChanges();

        resetFormChanges();

        registerComponent(testButton);
        registerComponent(infoButton);
        registerComponent(activeCheckBox);
    }

    public ConnectionSettings getTemporaryConfig() throws ConfigurationException {
        try {
            ConfigurationHandle.setTransitory(true);

            UserInterface.stopTableCellEditing(mainPanel);
            ConnectionSettings configuration = getConfiguration();
            ConnectionSettings clone = configuration.clone();
            clone.getDatabaseSettings().getAuthenticationInfo().setTemporary(true);

            ConnectionDatabaseSettingsForm databaseSettingsEditor = configuration.getDatabaseSettings().getSettingsEditor();
            if (databaseSettingsEditor != null) databaseSettingsEditor.applyFormChanges(clone.getDatabaseSettings());

            ConnectionPropertiesSettingsForm propertiesSettingsEditor = configuration.getPropertiesSettings().getSettingsEditor();
            if (propertiesSettingsEditor != null)
                propertiesSettingsEditor.applyFormChanges(clone.getPropertiesSettings());

            ConnectionSshTunnelSettingsForm sshTunnelSettingsForm = configuration.getSshTunnelSettings().getSettingsEditor();
            if (sshTunnelSettingsForm != null) sshTunnelSettingsForm.applyFormChanges(clone.getSshTunnelSettings());

            ConnectionSslSettingsForm sslSettingsForm = configuration.getSslSettings().getSettingsEditor();
            if (sslSettingsForm != null) sslSettingsForm.applyFormChanges(clone.getSslSettings());

            ConnectionDetailSettingsForm detailSettingsForm = configuration.getDetailSettings().getSettingsEditor();
            if (detailSettingsForm != null) detailSettingsForm.applyFormChanges(clone.getDetailSettings());

            ConnectionDebuggerSettingsForm debuggerSettingsForm = configuration.getDebuggerSettings().getSettingsEditor();
            if (debuggerSettingsForm != null) debuggerSettingsForm.applyFormChanges(clone.getDebuggerSettings());

            ConnectionFilterSettingsForm filterSettingsForm = configuration.getFilterSettings().getSettingsEditor();
            if (filterSettingsForm != null) filterSettingsForm.applyFormChanges(clone.getFilterSettings());

            return clone;
        } finally {
            ConfigurationHandle.setTransitory(false);
        }
    }

    @Override
    protected ActionListener createActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                ConnectionSettings configuration = getConfiguration();
                if (source == testButton || source == infoButton) {
                    ConnectionSettingsForm connectionSettingsForm = configuration.getSettingsEditor();
                    if (connectionSettingsForm != null) {
                        Project project = ensureProject();
                        try {
                            ConnectionSettings temporaryConfig = connectionSettingsForm.getTemporaryConfig();
                            ConnectionManager connectionManager = ConnectionManager.getInstance(project);

                            if (source == testButton) connectionManager.testConfigConnection(temporaryConfig, true);
                            if (source == infoButton) {
                                ConnectionDetailSettingsForm detailSettingsForm = configuration.getDetailSettings().getSettingsEditor();
                                if (detailSettingsForm != null) {
                                    EnvironmentType environmentType = detailSettingsForm.getSelectedEnvironmentType();
                                    connectionManager.showConnectionInfo(temporaryConfig, environmentType);
                                }
                            }
                            configuration.getDatabaseSettings().setConnectivityStatus(temporaryConfig.getDatabaseSettings().getConnectivityStatus());

                            refreshConnectionList(configuration);
                        } catch (ConfigurationException e1) {
                            conditionallyLog(e1);
                            Messages.showErrorDialog(project, "Configuration error", e1.getLocalizedMessage());
                        }
                    }
                }
                if (source == activeCheckBox) {
                    configuration.setModified(true);
                    refreshConnectionList(configuration);
                }

            }

            private void refreshConnectionList(ConnectionSettings configuration) {
                ConnectionBundleSettings bundleSettings = configuration.ensureParent();
                ConnectionBundleSettingsForm bundleSettingsEditor = bundleSettings.getSettingsEditor();
                if (bundleSettingsEditor == null) return;

                JList<?> connectionList = bundleSettingsEditor.getList();
                UserInterface.repaint(connectionList);
                ConnectionDatabaseSettingsForm settingsEditor = configuration.getDatabaseSettings().getSettingsEditor();
                if (settingsEditor == null) return;

                settingsEditor.notifyPresentationChanges();
            }
        };
    }

    public boolean isConnectionActive() {
        return activeCheckBox.isSelected();
    }

    public void selectTab(String tabName) {
        Safe.run(tabbedPane, t -> {
            for (TabInfo tabInfo : t.getTabs()) {
                if (!Objects.equals(tabName, tabInfo.getText())) {
                    continue;
                }

                tabbedPane.select(tabInfo, true);
            }
        });
    }

    public String getSelectedTabName() {
        return Safe.call(tabbedPane, t -> {
            TabInfo selectedInfo = t.getSelectedInfo();
            if (selectedInfo == null) {
                return null;
            }

            return selectedInfo.getText();
        });
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    @Override
    public void resetFormChanges() {
        activeCheckBox.setSelected(getConfiguration().isActive());
    }

    @Override
    public void applyFormChanges() throws ConfigurationException {
        UserInterface.stopTableCellEditing(mainPanel);
        applyFormChanges(getConfiguration());
    }

    @Override
    public void applyFormChanges(ConnectionSettings configuration) throws ConfigurationException {
        boolean settingsChanged = configuration.isActive() != activeCheckBox.isSelected();
        configuration.setActive(activeCheckBox.isSelected());

        SettingsChangeNotifier.register(() -> {
            if (settingsChanged) {
                ProjectEvents.notify(getProject(),
                        ConnectionConfigListener.TOPIC,
                        listener -> listener.connectionChanged(configuration.getConnectionId()));
            }
        });
    }
}
