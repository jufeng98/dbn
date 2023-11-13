package com.dbn.common.properties.ui;

import com.dbn.common.ui.form.DBNForm;
import com.dbn.common.ui.form.DBNFormBase;
import com.dbn.common.ui.util.UserInterface;
import com.dbn.common.dispose.Disposer;
import com.intellij.ui.ToolbarDecorator;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class PropertiesEditorForm extends DBNFormBase {
    private JPanel mainPanel;
    private final PropertiesEditorTable table;

    public PropertiesEditorForm(DBNForm parent, Map<String, String> properties, boolean showMoveButtons) {
        super(parent);
        table = new PropertiesEditorTable(this, properties);
        Disposer.register(this, table);

        ToolbarDecorator decorator = UserInterface.createToolbarDecorator(table);
        decorator.setAddAction(button -> table.insertRow());
        decorator.setRemoveAction(button -> table.removeRow());

        if (showMoveButtons) {
            decorator.setMoveUpAction(button -> table.moveRowUp());
            decorator.setMoveDownAction(button -> table.moveRowDown());
        }

        JPanel propertiesPanel = decorator.createPanel();
        Container parentContainer = table.getParent();
        parentContainer.setBackground(table.getBackground());
        mainPanel.add(propertiesPanel, BorderLayout.CENTER);
/*
        propertiesTableScrollPane.setViewportView(propertiesTable);
        propertiesTableScrollPane.setPreferredSize(new Dimension(200, 80));
*/
    }

    public PropertiesEditorTable getTable() {
        return table;
    }

    public void setProperties(Map<String, String> properties) {
        table.setProperties(properties);
    } 

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    public Map<String, String> getProperties() {
        return table.getModel().exportProperties();
    }
}
