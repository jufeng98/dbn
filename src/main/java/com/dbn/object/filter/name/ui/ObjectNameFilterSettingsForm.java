package com.dbn.object.filter.name.ui;

import com.dbn.browser.options.ObjectFilterChangeListener;
import com.dbn.common.event.ProjectEvents;
import com.dbn.common.options.SettingsChangeNotifier;
import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.common.ui.util.Keyboard;
import com.dbn.common.ui.util.Mouse;
import com.dbn.common.util.Actions;
import com.dbn.object.filter.name.*;
import com.dbn.object.filter.name.action.*;
import com.dbn.object.type.DBObjectType;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

@Deprecated
public class ObjectNameFilterSettingsForm extends ConfigurationEditorForm<ObjectNameFilterSettings> {
    private JPanel mainPanel;
    private JTree filtersTree;
    private JPanel actionsPanel;

    public ObjectNameFilterSettingsForm(ObjectNameFilterSettings configuration) {
        super(configuration);

/*        ObjectNameFilter schemaFilter = new ObjectNameFilter(DBObjectType.SCHEMA, ConditionOperator.NOT_LIKE, "T%");

        schemaFilter.addCondition(ConditionOperator.LIKE, "AE9%");
        configuration.addFilter(schemaFilter);

        ObjectNameFilter tableFilter = new ObjectNameFilter(DBObjectType.TABLE, ConditionOperator.NOT_LIKE, "T%");
        SimpleFilterCondition filterX = new SimpleFilterCondition(ConditionOperator.NOT_LIKE, "ZZ_%");
        tableFilter.addCondition(filterX);
        filterX.joinCondition(ConditionOperator.EQUAL, "BLA");

        configuration.addFilter(tableFilter);*/

        ActionToolbar actionToolbar = Actions.createActionToolbar(actionsPanel,
                "DBNavigator.ObjectNameFilters.Setup", true,
                new FilterCreateAction(this),
                new FilterConditionCreateAction(this),
                new FilterConditionRemoveAction(this),
                new FilterConditionJoinTypeSwitchAction(this),
                new Separator(),
                new FilterConditionMoveUpAction(this),
                new FilterConditionMoveDownAction(this));
        actionsPanel.add(actionToolbar.getComponent());

        filtersTree.setCellRenderer(new FilterSettingsTreeCellRenderer());
        ObjectNameFilterSettings tableModel = configuration;

        filtersTree.setModel(tableModel);
        filtersTree.setShowsRootHandles(true);
        filtersTree.setRootVisible(false);

        for (ObjectNameFilter filter : tableModel.getFilters()) {
            filtersTree.expandPath(tableModel.createTreePath(filter));
        }

        filtersTree.addMouseListener(Mouse.listener().onClick(e -> {
            if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() > 1) {
                Object selection = getSelection();
                if (selection instanceof SimpleNameFilterCondition) {
                    SimpleNameFilterCondition condition = (SimpleNameFilterCondition) selection;
                    getManager().editFilterCondition(condition, ObjectNameFilterSettingsForm.this);
                }
            }
        }));

        filtersTree.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char key = e.getKeyChar();
                if (key == Keyboard.Key.ENTER) {
                    Object selection = getSelection();
                    if (selection instanceof SimpleNameFilterCondition) {
                        SimpleNameFilterCondition condition = (SimpleNameFilterCondition) selection;
                        getManager().editFilterCondition(condition, ObjectNameFilterSettingsForm.this);
                    }
                } else if (key == Keyboard.Key.DELETE) {
                    Object selection = getSelection();
                    if (selection instanceof FilterCondition) {
                        FilterCondition condition = (FilterCondition) selection;
                        getManager().removeFilterCondition(condition, ObjectNameFilterSettingsForm.this);
                    }
                }
            }
        });
    }

    private ObjectNameFilterManager getManager() {
        return ObjectNameFilterManager.getInstance(getConfiguration().getProject());
    }

    public Object getSelection() {
        TreePath selectionPath = filtersTree.getSelectionPath();
        return selectionPath == null ? null : selectionPath.getLastPathComponent();
    }


    public JTree getFiltersTree() {
        return filtersTree;
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    @Override
    public void applyFormChanges() throws ConfigurationException {
        ObjectNameFilterSettings configuration = getConfiguration();
        boolean notifyFilterListeners = configuration.isModified();

        // collect before after applying the changes
        Set<DBObjectType> filterObjectTypes = new HashSet<>();
        filterObjectTypes.addAll(configuration.getFilteredObjectTypes());

        Element element = new Element("Temp");
        ObjectNameFilterSettings tempSettings = (ObjectNameFilterSettings) filtersTree.getModel();
        tempSettings.writeConfiguration(element);
        configuration.readConfiguration(element);
        // after applying the changes
        filterObjectTypes.addAll(configuration.getFilteredObjectTypes());

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
    public void resetFormChanges() {}
}
