package com.dbn.object.properties.ui;

import com.dbn.browser.DatabaseBrowserManager;
import com.dbn.browser.model.BrowserTreeEventListener;
import com.dbn.browser.model.BrowserTreeNode;
import com.dbn.browser.ui.DatabaseBrowserTree;
import com.dbn.common.dispose.Disposer;
import com.dbn.common.environment.EnvironmentType;
import com.dbn.common.environment.options.EnvironmentSettings;
import com.dbn.common.event.ProjectEvents;
import com.dbn.common.thread.Background;
import com.dbn.common.thread.Dispatch;
import com.dbn.common.thread.PooledThread;
import com.dbn.common.ui.form.DBNForm;
import com.dbn.common.ui.form.DBNFormBase;
import com.dbn.common.ui.util.UserInterface;
import com.dbn.common.util.Actions;
import com.dbn.common.util.Naming;
import com.dbn.object.common.DBObject;
import com.dbn.object.lookup.DBObjectRef;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ObjectPropertiesForm extends DBNFormBase {
    private JPanel mainPanel;
    private JLabel objectLabel;
    private JLabel objectTypeLabel;
    private JScrollPane objectPropertiesScrollPane;
    private JPanel closeActionPanel;
    private JPanel headerPanel;
    private DBObjectRef<?> object;

    private final AtomicReference<PooledThread> refreshHandle = new AtomicReference<>();
    private final ObjectPropertiesTable objectPropertiesTable;

    public ObjectPropertiesForm(DBNForm parent) {
        super(parent);
        //ActionToolbar objectPropertiesActionToolbar = Actions.createActionToolbar(closeActionPanel, "DBNavigator.ActionGroup.Browser.ObjectProperties", "", true);
        //closeActionPanel.add(objectPropertiesActionToolbar.getComponent(), BorderLayout.EAST);
        objectPropertiesTable = new ObjectPropertiesTable(this, new ObjectPropertiesTableModel());
        objectPropertiesScrollPane.setViewportView(objectPropertiesTable);
        objectTypeLabel.setText("Object properties:");
        objectLabel.setText("(no object selected)");

        Project project = ensureProject();
        ProjectEvents.subscribe(project, this, BrowserTreeEventListener.TOPIC, browserTreeEventListener());
    }

    @NotNull
    private BrowserTreeEventListener browserTreeEventListener() {
        return new BrowserTreeEventListener() {
            @Override
            public void selectionChanged() {
                Project project = ensureProject();
                DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(project);
                if (!browserManager.getShowObjectProperties().value()) return;

                List<DBObject> selectedObjects = browserManager.getSelectedObjects();
                DBObject selectedObject = selectedObjects.size() == 1 ? selectedObjects.get(0) : null;
                setObject(selectedObject);
            }
        };
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    public DBObject getObject() {
        return DBObjectRef.get(object);
    }

    public void setObject(@Nullable DBObject object) {
        DBObject localObject = getObject();
        if (Objects.equals(object, localObject)) return;

        this.object = object == null ? null : DBObjectRef.of(object);
        Background.run(getProject(), refreshHandle, () -> {
            ObjectPropertiesTableModel tableModel = object == null ?
                    new ObjectPropertiesTableModel() :
                    new ObjectPropertiesTableModel(object.getPresentableProperties());
            Disposer.register(ObjectPropertiesForm.this, tableModel);

            Dispatch.run(() -> {
                if (object == null) {
                    objectTypeLabel.setText("Object properties:");
                    objectLabel.setText("(no object selected)");
                    objectLabel.setIcon(null);
                    UserInterface.setBackgroundRecursive(headerPanel, EnvironmentType.DEFAULT.getColor());
                } else {
                    objectLabel.setText(object.getName());
                    objectLabel.setIcon(object.getIcon());
                    objectTypeLabel.setText(Naming.capitalize(object.getTypeName()) + ":");
                    UserInterface.setBackgroundRecursive(headerPanel, object.getEnvironmentType().getColor());
                }

                ObjectPropertiesTableModel oldTableModel = (ObjectPropertiesTableModel) objectPropertiesTable.getModel();
                objectPropertiesTable.setModel(tableModel);
                objectPropertiesTable.accommodateColumnsSize();


                UserInterface.repaint(mainPanel);
                Disposer.dispose(oldTableModel);
            });
        });
    }
}
