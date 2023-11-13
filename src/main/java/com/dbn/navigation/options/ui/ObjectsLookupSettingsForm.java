package com.dbn.navigation.options.ui;

import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.common.ui.Presentable;
import com.dbn.common.ui.list.CheckBoxList;
import com.dbn.common.ui.util.Keyboard;
import com.dbn.navigation.options.ObjectsLookupSettings;
import com.intellij.openapi.actionSystem.Shortcut;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import static com.dbn.common.ui.util.ComboBoxes.*;

public class ObjectsLookupSettingsForm extends ConfigurationEditorForm<ObjectsLookupSettings> {
    private JPanel mainPanel;
    private JScrollPane lookupObjectsScrollPane;
    private JComboBox<ConnectionOption> connectionComboBox;
    private JComboBox<BehaviorOption> behaviorComboBox;
    private final CheckBoxList lookupObjectsList;

    public ObjectsLookupSettingsForm(ObjectsLookupSettings configuration) {
        super(configuration);
        Shortcut[] shortcuts = Keyboard.getShortcuts("DBNavigator.Actions.Navigation.GotoDatabaseObject");
        TitledBorder border = (TitledBorder) mainPanel.getBorder();
        border.setTitle("Lookup Objects (" + KeymapUtil.getShortcutsText(shortcuts) + ")");

        initComboBox(connectionComboBox,
                ConnectionOption.PROMPT,
                ConnectionOption.RECENT);

        initComboBox(behaviorComboBox,
                BehaviorOption.LOOKUP,
                BehaviorOption.LOAD);

        lookupObjectsList = new CheckBoxList(configuration.getLookupObjectTypes());
        lookupObjectsScrollPane.setViewportView(lookupObjectsList);

        resetFormChanges();
        registerComponents(mainPanel);
    }

    @Override
    public void applyFormChanges() throws ConfigurationException {
        lookupObjectsList.applyChanges();
        ObjectsLookupSettings configuration = getConfiguration();
        configuration.getForceDatabaseLoad().setValue(getSelection(behaviorComboBox).getValue());
        configuration.getPromptConnectionSelection().setValue(getSelection(connectionComboBox).getValue());
    }

    @Override
    public void resetFormChanges() {
        ObjectsLookupSettings configuration = getConfiguration();
        if (configuration.getForceDatabaseLoad().getValue())
            setSelection(behaviorComboBox, BehaviorOption.LOAD); else
            setSelection(behaviorComboBox, BehaviorOption.LOOKUP);

        if (configuration.getPromptConnectionSelection().getValue())
            setSelection(connectionComboBox, ConnectionOption.PROMPT); else
            setSelection(connectionComboBox, ConnectionOption.RECENT);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    private enum ConnectionOption implements Presentable {
        PROMPT("Prompt connection selection", true),
        RECENT("Select most recently used connection", false);

        private String name;
        private boolean value;

        ConnectionOption(String name, boolean value) {
            this.name = name;
            this.value = value;
        }

        @NotNull
        @Override
        public String getName() {
            return name;
        }

        @Nullable
        @Override
        public String getDescription() {
            return null;
        }


        @Nullable
        @Override
        public Icon getIcon() {
            return null;
        }

        public boolean getValue() {
            return value;
        }
    }

    private enum BehaviorOption implements Presentable {
        LOOKUP("Lookup loaded objects only", false),
        LOAD("Force database load (slow)", true);

        private String name;
        private boolean value;

        BehaviorOption(String name, boolean value) {
            this.name = name;
            this.value = value;
        }


        @NotNull
        @Override
        public String getName() {
            return name;
        }

        public boolean getValue() {
            return value;
        }
    }
}
