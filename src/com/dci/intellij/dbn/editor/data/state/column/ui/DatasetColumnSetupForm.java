package com.dci.intellij.dbn.editor.data.state.column.ui;

import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.common.ui.DBNHeaderForm;
import com.dci.intellij.dbn.common.ui.list.CheckBoxList;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.dci.intellij.dbn.editor.data.state.column.DatasetColumnSetup;
import com.dci.intellij.dbn.editor.data.state.column.DatasetColumnState;
import com.dci.intellij.dbn.editor.data.state.column.action.MoveDownAction;
import com.dci.intellij.dbn.editor.data.state.column.action.MoveUpAction;
import com.dci.intellij.dbn.editor.data.state.column.action.OrderAlphabeticallyAction;
import com.dci.intellij.dbn.editor.data.state.column.action.RevertColumnOrderAction;
import com.dci.intellij.dbn.editor.data.state.column.action.SelectAllColumnsAction;
import com.dci.intellij.dbn.object.DBDataset;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.UIUtil;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatasetColumnSetupForm extends DBNFormImpl {
    private JPanel mainPanel;
    private JPanel actionPanel;
    private JBScrollPane columnListScrollPane;
    private JPanel headerPanel;
    private CheckBoxList<ColumnStateSelectable> columnList;
    private DatasetColumnSetup columnSetup;

    public DatasetColumnSetupForm(DatasetEditor datasetEditor) {
        DBDataset dataset = datasetEditor.getDataset();
        this.columnSetup = datasetEditor.getState().getColumnSetup();
        List<DatasetColumnState> columnStates = columnSetup.getColumnStates();
        List<ColumnStateSelectable> columnStateSel = new ArrayList<ColumnStateSelectable>();
        for (DatasetColumnState columnState : columnStates) {
            columnStateSel.add(new ColumnStateSelectable(dataset, columnState));
        }

        columnList = new CheckBoxList<ColumnStateSelectable>(columnStateSel, true);
        columnListScrollPane.setViewportView(columnList);

        ActionToolbar actionToolbar = ActionUtil.createActionToolbar("", false,
                new SelectAllColumnsAction(columnList),
                ActionUtil.SEPARATOR,
                new MoveUpAction(columnList),
                new MoveDownAction(columnList),
                ActionUtil.SEPARATOR,
                new OrderAlphabeticallyAction(columnList),
                new RevertColumnOrderAction(columnList));
        actionPanel.add(actionToolbar.getComponent(), BorderLayout.WEST);

        createHeaderForm(dataset);
    }

    private void createHeaderForm(DBDataset dataset) {
        String headerTitle = dataset.getQualifiedName();
        Icon headerIcon = dataset.getIcon();
        Color headerBackground = UIUtil.getPanelBackground();
        if (getEnvironmentSettings(dataset.getProject()).getVisibilitySettings().getDialogHeaders().value()) {
            headerBackground = dataset.getEnvironmentType().getColor();
        }
        DBNHeaderForm headerForm = new DBNHeaderForm(
                headerTitle,
                headerIcon,
                headerBackground);
        headerPanel.add(headerForm.getComponent(), BorderLayout.CENTER);
    }

    @Override
    public JComponent getComponent() {
        return mainPanel;
    }

    public boolean applyChanges(){
        boolean changed = columnList.applyChanges();
        ListModel model = columnList.getModel();
        for(int i=0; i<model.getSize(); i++ ) {
            ColumnStateSelectable columnState = columnList.getElementAt(i);
            changed = changed || columnState.getPosition() != i;
            columnState.setPosition(i);
        }
        Collections.sort(columnSetup.getColumnStates());
        return changed;
    }

}
