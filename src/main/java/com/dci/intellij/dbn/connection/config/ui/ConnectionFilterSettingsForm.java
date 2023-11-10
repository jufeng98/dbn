package com.dci.intellij.dbn.connection.config.ui;

import com.dci.intellij.dbn.browser.options.ObjectFilterChangeListener;
import com.dci.intellij.dbn.common.constant.Constant;
import com.dci.intellij.dbn.common.event.ProjectEvents;
import com.dci.intellij.dbn.common.options.SettingsChangeNotifier;
import com.dci.intellij.dbn.common.options.ui.CompositeConfigurationEditorForm;
import com.dci.intellij.dbn.connection.ConnectionId;
import com.dci.intellij.dbn.connection.config.ConnectionFilterSettings;
import com.dci.intellij.dbn.object.type.DBObjectType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ConnectionFilterSettingsForm extends CompositeConfigurationEditorForm<ConnectionFilterSettings> {
    private JPanel mainPanel;
    private JPanel objectTypesFilterPanel;
    private JPanel objectNameFiltersPanel;
    private JCheckBox hideEmptySchemasCheckBox;
    private JCheckBox hideAuditColumnsCheckBox;
    private JCheckBox hidePseudoColumnsCheckBox;

    public ConnectionFilterSettingsForm(ConnectionFilterSettings settings) {
        super(settings);
        objectTypesFilterPanel.add(settings.getObjectTypeFilterSettings().createComponent(), BorderLayout.CENTER);
        objectNameFiltersPanel.add(settings.getObjectNameFilterSettings().createComponent(), BorderLayout.CENTER);

        hideEmptySchemasCheckBox.setSelected(settings.isHideEmptySchemas());
        hideAuditColumnsCheckBox.setSelected(settings.isHideAuditColumns());
        hidePseudoColumnsCheckBox.setSelected(settings.isHidePseudoColumns());

        registerComponent(hideEmptySchemasCheckBox);
        registerComponent(hideAuditColumnsCheckBox);
        registerComponent(hidePseudoColumnsCheckBox);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    @Override
    public void applyFormChanges() throws ConfigurationException {
        ConnectionFilterSettings configuration = getConfiguration();
        boolean notifyFilterListenersSchemas = configuration.isHideEmptySchemas() != hideEmptySchemasCheckBox.isSelected();
        boolean notifyFilterListenersColumns =
                configuration.isHideAuditColumns() != hideAuditColumnsCheckBox.isSelected() ||
                configuration.isHidePseudoColumns() != hidePseudoColumnsCheckBox.isSelected();

        applyFormChanges(configuration);

        Project project = configuration.getProject();
        SettingsChangeNotifier.register(() -> {
            ConnectionId connectionId = configuration.getConnectionId();
            if (notifyFilterListenersSchemas) {
                ProjectEvents.notify(project,
                        ObjectFilterChangeListener.TOPIC,
                        (listener) -> listener.nameFiltersChanged(connectionId, Constant.array(DBObjectType.SCHEMA)));
            }
            if (notifyFilterListenersColumns) {
                ProjectEvents.notify(project,
                    ObjectFilterChangeListener.TOPIC,
                    (listener) -> listener.nameFiltersChanged(connectionId, Constant.array(DBObjectType.COLUMN)));
            }
        });
    }

    @Override
    public void applyFormChanges(ConnectionFilterSettings configuration) throws ConfigurationException {
        configuration.setHideEmptySchemas(hideEmptySchemasCheckBox.isSelected());
        configuration.setHideAuditColumns(hideAuditColumnsCheckBox.isSelected());
        configuration.setHidePseudoColumns(hidePseudoColumnsCheckBox.isSelected());
    }
}
