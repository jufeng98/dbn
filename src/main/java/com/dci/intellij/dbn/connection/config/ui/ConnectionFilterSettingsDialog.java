package com.dci.intellij.dbn.connection.config.ui;

import com.dci.intellij.dbn.common.options.ConfigurationHandle;
import com.dci.intellij.dbn.common.ui.dialog.DBNDialog;
import com.dci.intellij.dbn.common.ui.form.DBNContentWithHeaderForm;
import com.dci.intellij.dbn.common.ui.form.DBNForm;
import com.dci.intellij.dbn.common.ui.form.DBNHeaderForm;
import com.dci.intellij.dbn.common.util.Messages;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionRef;
import com.dci.intellij.dbn.connection.config.ConnectionFilterSettings;
import com.dci.intellij.dbn.connection.config.ConnectionSettings;
import com.dci.intellij.dbn.options.ProjectSettingsManager;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static com.dci.intellij.dbn.diagnostics.Diagnostics.conditionallyLog;

public class ConnectionFilterSettingsDialog extends DBNDialog<DBNContentWithHeaderForm> {
    private final ConnectionRef connection;
    private ConnectionFilterSettingsForm configurationEditor;
    private ConnectionFilterSettings filterSettings;

    public ConnectionFilterSettingsDialog(@NotNull ConnectionHandler connection) {
        super(connection.getProject(), "Object filters", true);
        this.connection = connection.ref();
        setModal(true);
        setResizable(true);
        init();
    }

    @NotNull
    @Override
    protected DBNContentWithHeaderForm createForm() {
        ConnectionHandler connection = this.connection.ensure();
        return new DBNContentWithHeaderForm(this) {
            @Override
            public DBNHeaderForm createHeaderForm() {
                return new DBNHeaderForm(this, connection);
            }

            @Override
            public DBNForm createContentForm() {
                ProjectSettingsManager settingsManager = ProjectSettingsManager.getInstance(ensureProject());
                ConnectionSettings connectionSettings = settingsManager.getConnectionSettings().getConnectionSettings(connection.getConnectionId());
                filterSettings = connectionSettings.getFilterSettings();
                configurationEditor = filterSettings.createConfigurationEditor();
                return configurationEditor;
            }
        };
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        return new Action[]{
                getOKAction(),
                getCancelAction()
        };
    }

    @Override
    public void doOKAction() {
        try {
            // !!workaround!! apply settings is normally cascaded from top level settings
            configurationEditor.applyFormChanges();
            filterSettings.apply();
            ConfigurationHandle.notifyChanges();
            super.doOKAction();
        } catch (ConfigurationException e) {
            conditionallyLog(e);
            Messages.showErrorDialog(getProject(), "Configuration error", e.getMessage());
        }

    }

    @Override
    public void doCancelAction() {
        super.doCancelAction();
    }

    @Override
    public void disposeInner() {
        filterSettings.disposeUIResources();
    }
}
