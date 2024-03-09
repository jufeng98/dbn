package com.dbn.object.filter.custom.ui;

import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.common.ui.ValueSelector;
import com.dbn.common.ui.ValueSelectorOption;
import com.dbn.common.ui.misc.DBNTableScrollPane;
import com.dbn.common.util.Dialogs;
import com.dbn.object.filter.custom.ObjectCustomFilterSettings;
import com.dbn.object.filter.custom.ObjectFilter;
import com.dbn.object.type.DBObjectType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.PlatformIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ObjectFilterSettingsForm extends ConfigurationEditorForm<ObjectCustomFilterSettings> {
    private JPanel mainPanel;
    private JPanel actionsPanel;
    private DBNTableScrollPane filterTableScrollPane;

    public ObjectFilterSettingsForm(ObjectCustomFilterSettings configuration) {
        super(configuration);

        ObjectFiltersTable filtersTable = new ObjectFiltersTable(this, configuration);
        filterTableScrollPane.setViewportView(filtersTable);

        actionsPanel.add(new ObjectTypeSelector(), BorderLayout.CENTER);
    }

    private class ObjectTypeSelector extends ValueSelector<DBObjectType> {
        ObjectTypeSelector() {
            super(PlatformIcons.ADD_ICON, "Add Filter", null, ValueSelectorOption.HIDE_DESCRIPTION);
            addListener((oldValue, newValue) -> createFilter(newValue));
        }

        @Override
        public List<DBObjectType> loadValues() {
            return Arrays.asList(
                    DBObjectType.SCHEMA,
                    DBObjectType.TABLE,
                    DBObjectType.VIEW,
                    DBObjectType.COLUMN,
                    DBObjectType.CONSTRAINT,
                    DBObjectType.INDEX);
        }
    }

    private void createFilter(DBObjectType objectType) {
        ObjectCustomFilterSettings filterSettings = getConfiguration();
        ObjectFilter filter = new ObjectFilter(filterSettings);
        filter.setObjectType(objectType);

        Dialogs.show(() -> new ObjectFilterDetailsDialog(filter, true));
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    @Override
    public void applyFormChanges() throws ConfigurationException {

    }

    @Override
    public void resetFormChanges() {

    }
}
