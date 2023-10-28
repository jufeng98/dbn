package com.dci.intellij.dbn.common.ui.list;

import com.dci.intellij.dbn.common.ui.component.DBNComponent;
import com.dci.intellij.dbn.common.ui.form.DBNFormBase;
import com.intellij.ui.ToolbarDecorator;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.dci.intellij.dbn.common.ui.util.UserInterface.createToolbarDecorator;

public class EditableStringListForm extends DBNFormBase {
    private JPanel component;
    private JLabel titleLabel;
    private JPanel listPanel;

    private final EditableStringList editableStringList;

    public EditableStringListForm(DBNComponent parent, String title, boolean sorted) {
        this(parent, title, new ArrayList<>(), sorted);
    }

    public EditableStringListForm(DBNComponent parent, String title, List<String> elements, boolean sorted) {
        super(parent);
        editableStringList = new EditableStringList(this, elements, sorted, false);
        ToolbarDecorator decorator = createToolbarDecorator(editableStringList);
        decorator.setAddAction(anActionButton -> editableStringList.insertRow());
        decorator.setRemoveAction(anActionButton -> editableStringList.removeRow());
        decorator.setMoveUpAction(anActionButton -> editableStringList.moveRowUp());
        decorator.setMoveDownAction(anActionButton -> editableStringList.moveRowDown());
        titleLabel.setText(title);
        //decorator.setPreferredSize(new Dimension(200, 300));
        JPanel editableListPanel = decorator.createPanel();
        Container parentContainer = editableStringList.getParent();
        parentContainer.setBackground(editableStringList.getBackground());
        this.listPanel.add(editableListPanel, BorderLayout.CENTER);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return component;
    }

    public List<String> getStringValues() {
        return editableStringList.getStringValues();
    }

    public void setStringValues(List<String> stringValues) {
        editableStringList.setStringValues(stringValues);
    }
}
