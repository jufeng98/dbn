package com.dci.intellij.dbn.data.grid.options.ui;

import com.dci.intellij.dbn.common.event.ProjectEvents;
import com.dci.intellij.dbn.common.options.SettingsChangeNotifier;
import com.dci.intellij.dbn.common.options.ui.ConfigurationEditorForm;
import com.dci.intellij.dbn.common.ui.list.EditableStringListForm;
import com.dci.intellij.dbn.data.grid.options.DataGridAuditColumnSettings;
import com.dci.intellij.dbn.data.grid.options.DataGridSettingsChangeListener;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class DataGridAuditColumnSettingsForm extends ConfigurationEditorForm<DataGridAuditColumnSettings> {
    private JPanel mainPanel;
    private JCheckBox visibleCheckBox;
    private JCheckBox editableCheckBox;
    private JPanel columnNameListPanel;

    private EditableStringListForm editableStringListForm;

    public DataGridAuditColumnSettingsForm(DataGridAuditColumnSettings settings) {
        super(settings);
        editableStringListForm = new EditableStringListForm(this, "Audit column names", true);
        JComponent listComponent = editableStringListForm.getComponent();
        columnNameListPanel.add(listComponent, BorderLayout.CENTER);

        resetFormChanges();
        editableCheckBox.setEnabled(visibleCheckBox.isSelected());
        registerComponent(mainPanel);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    @Override
    protected ActionListener createActionListener() {
        return e -> {
            getConfiguration().setModified(true);
            if (e.getSource() == visibleCheckBox) {
                editableCheckBox.setEnabled(visibleCheckBox.isSelected());
            }
        };
    }

    @Override
    public void applyFormChanges() throws ConfigurationException {
        DataGridAuditColumnSettings configuration = getConfiguration();
        boolean auditColumnsVisible = visibleCheckBox.isSelected();
        boolean visibilityChanged = configuration.isShowColumns() != auditColumnsVisible;
        configuration.setShowColumns(auditColumnsVisible);
        configuration.setAllowEditing(editableCheckBox.isSelected());
        configuration.setColumnNames(editableStringListForm.getStringValues());

        Project project = configuration.getProject();
        SettingsChangeNotifier.register(() -> {
            if (!visibilityChanged) return;
            ProjectEvents.notify(project,
                    DataGridSettingsChangeListener.TOPIC,
                    (listener) -> listener.auditDataVisibilityChanged(auditColumnsVisible));
        });
    }

    @Override
    public void resetFormChanges() {
        DataGridAuditColumnSettings settings = getConfiguration();
        visibleCheckBox.setSelected(settings.isShowColumns());
        editableCheckBox.setSelected(settings.isAllowEditing());
        List<String> columnNamesSet = settings.getColumnNames();
        editableStringListForm.setStringValues(columnNamesSet);
    }
}
