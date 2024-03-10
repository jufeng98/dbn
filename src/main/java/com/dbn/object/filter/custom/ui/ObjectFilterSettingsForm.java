package com.dbn.object.filter.custom.ui;

import com.dbn.browser.options.ObjectFilterChangeListener;
import com.dbn.common.event.ProjectEvents;
import com.dbn.common.options.SettingsChangeNotifier;
import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.common.ui.ValueSelector;
import com.dbn.common.ui.ValueSelectorOption;
import com.dbn.common.ui.misc.DBNTableScrollPane;
import com.dbn.common.util.Dialogs;
import com.dbn.object.filter.custom.ObjectFilter;
import com.dbn.object.filter.custom.ObjectFilterSettings;
import com.dbn.object.type.DBObjectType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.util.PlatformIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ObjectFilterSettingsForm extends ConfigurationEditorForm<ObjectFilterSettings> {
    private JPanel mainPanel;
    private JPanel actionsPanel;
    private DBNTableScrollPane filterTableScrollPane;
    private final ObjectFiltersTable filtersTable;

    public ObjectFilterSettingsForm(ObjectFilterSettings configuration) {
        super(configuration);

        filtersTable = new ObjectFiltersTable(this, configuration);
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
        ObjectFilterSettings filterSettings = getConfiguration();
        ObjectFilter<?> filter = new ObjectFilter<>(filterSettings);
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
        ObjectFilterSettings configuration = getConfiguration();
        boolean notifyFilterListeners = configuration.isModified();

        // capture before applying changes (consider deleted filters)
        Set<DBObjectType> filterObjectTypes = new HashSet<>(configuration.getFilterObjectTypes());

        List<ObjectFilter<?>> filters = filtersTable.getModel().getFilters();
        getConfiguration().setFilters(filters);

        // capture after applying changes
        filterObjectTypes.addAll(configuration.getFilterObjectTypes());

        Project project = configuration.getProject();
        SettingsChangeNotifier.register(() -> {
            if (notifyFilterListeners) {
                DBObjectType[] refreshObjectTypes = filterObjectTypes.toArray(new DBObjectType[0]);
                ProjectEvents.notify(project, ObjectFilterChangeListener.TOPIC,
                        (listener) -> listener.nameFiltersChanged(configuration.getConnectionId(), refreshObjectTypes));
            }
        });
    }

    @Override
    public void resetFormChanges() {
    }
}
