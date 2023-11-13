package com.dbn.object.filter.type.ui;

import com.dbn.browser.options.ObjectFilterChangeListener;
import com.dbn.common.event.ProjectEvents;
import com.dbn.common.options.SettingsChangeNotifier;
import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.common.ui.list.CheckBoxList;
import com.dbn.common.ui.util.UserInterface;
import com.dbn.connection.ConnectionId;
import com.dbn.object.filter.type.ObjectTypeFilterSetting;
import com.dbn.object.filter.type.ObjectTypeFilterSettings;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ObjectTypeFilterSettingsForm extends ConfigurationEditorForm<ObjectTypeFilterSettings> {
    private JPanel mainPanel;
    private JScrollPane visibleObjectsScrollPane;
    private JCheckBox useMasterSettingsCheckBox;
    private JLabel visibleObjectTypesLabel;
    private CheckBoxList<ObjectTypeFilterSetting> visibleObjectsList;

    public ObjectTypeFilterSettingsForm(ObjectTypeFilterSettings configuration) {
        super(configuration);

        visibleObjectsList = new CheckBoxList<>(configuration.getSettings());
        visibleObjectsScrollPane.setViewportView(visibleObjectsList);

        boolean masterSettingsAvailable = configuration.getMasterSettings() != null;
        useMasterSettingsCheckBox.setVisible(masterSettingsAvailable);
        if (masterSettingsAvailable) {
            visibleObjectTypesLabel.setVisible(false);
            useMasterSettingsCheckBox.addActionListener(e -> {
                boolean enabled = !useMasterSettingsCheckBox.isSelected();
                visibleObjectsList.setEnabled(enabled);
                Color background = enabled ? UIUtil.getListBackground() : UIUtil.getComboBoxDisabledBackground();
                visibleObjectsList.setBackground(background);
                visibleObjectsList.clearSelection();
                visibleObjectsScrollPane.setVisible(enabled);
                UserInterface.repaint(mainPanel);
            });
        } else {
            mainPanel.setBorder(null);
        }
        configuration.getUseMasterSettings().from(useMasterSettingsCheckBox);
        boolean enabled = !masterSettingsAvailable || !useMasterSettingsCheckBox.isSelected();
        visibleObjectsList.setEnabled(enabled);
        visibleObjectsList.setBackground(enabled ? UIUtil.getListBackground() : UIUtil.getComboBoxDisabledBackground());
        visibleObjectsScrollPane.setVisible(enabled);

        registerComponents(visibleObjectsList, useMasterSettingsCheckBox);
    }


    public boolean isSelected(ObjectTypeFilterSetting objectFilterEntry) {
        return visibleObjectsList.isSelected(objectFilterEntry);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    @Override
    public void applyFormChanges() throws ConfigurationException {
        ObjectTypeFilterSettings configuration = getConfiguration();
        boolean notifyFilterListeners = configuration.isModified();
        visibleObjectsList.applyChanges();
        configuration.getUseMasterSettings().to(useMasterSettingsCheckBox);

        Project project = configuration.getProject();
        SettingsChangeNotifier.register(() -> {
             if (notifyFilterListeners) {
                 ConnectionId connectionId = configuration.getConnectionId();
                 ProjectEvents.notify(project,
                         ObjectFilterChangeListener.TOPIC,
                         (listener) -> listener.typeFiltersChanged(connectionId));
             }
         });
    }

    @Override
    public void resetFormChanges() {}
}
