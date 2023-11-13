package com.dbn.editor.data.options.ui;

import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.data.record.navigation.RecordNavigationTarget;
import com.dbn.editor.data.options.DataEditorRecordNavigationSettings;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static com.dbn.common.ui.util.ComboBoxes.*;

public class DataEditorRecordNavigationSettingsForm extends ConfigurationEditorForm<DataEditorRecordNavigationSettings> {
    private JPanel mainPanel;
    private JComboBox<RecordNavigationTarget> navigationTargetComboBox;


    public DataEditorRecordNavigationSettingsForm(DataEditorRecordNavigationSettings configuration) {
        super(configuration);
        initComboBox(navigationTargetComboBox,
                RecordNavigationTarget.EDITOR,
                RecordNavigationTarget.VIEWER,
                RecordNavigationTarget.ASK);

        resetFormChanges();
        registerComponent(mainPanel);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    @Override
    public void applyFormChanges() throws ConfigurationException {
        DataEditorRecordNavigationSettings configuration = getConfiguration();

        RecordNavigationTarget navigationTarget = getSelection(navigationTargetComboBox);
        configuration.setNavigationTarget(navigationTarget);
    }

    @Override
    public void resetFormChanges() {
        DataEditorRecordNavigationSettings configuration = getConfiguration();
        RecordNavigationTarget navigationTarget = configuration.getNavigationTarget();
        setSelection(navigationTargetComboBox, navigationTarget);
    }
}
