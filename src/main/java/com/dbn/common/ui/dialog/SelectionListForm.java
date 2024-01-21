package com.dbn.common.ui.dialog;

import com.dbn.common.ui.form.DBNFormBase;
import com.dbn.common.ui.form.DBNHeaderForm;
import com.intellij.ui.components.JBList;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SelectionListForm<T> extends DBNFormBase {
    private JPanel mainPanel;
    private JPanel headerPanel;
    private @Getter JBList<T> selectionList;

    public SelectionListForm(SelectionListDialog<T> dialog, @Nullable Object contextObject) {
        super(dialog);

        if (contextObject == null) {
            headerPanel.setVisible(false);
        } else {
            DBNHeaderForm headerForm = new DBNHeaderForm(this, contextObject);
            headerPanel.add(headerForm.getComponent(), BorderLayout.CENTER);
        }


        List<T> elements = dialog.getElements();
        T selection = dialog.getInitialSelection();

        DefaultListModel<T> model = new DefaultListModel<>();
        elements.forEach(e -> model.addElement(e));

        selectionList.setModel(model);
        selectionList.setSelectedValue(selection == null ? elements.get(0) : selection, true);
        selectionList.setCellRenderer(new DefaultListCellRenderer());
    }

    @Override
    protected JComponent getMainComponent() {
        return mainPanel;
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return selectionList;
    }
}
