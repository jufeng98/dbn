package com.dci.intellij.dbn.editor.data.state.sorting.ui;

import com.dci.intellij.dbn.common.color.Colors;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.ui.ValueSelector;
import com.dci.intellij.dbn.common.ui.ValueSelectorOption;
import com.dci.intellij.dbn.common.ui.form.DBNFormBase;
import com.dci.intellij.dbn.common.ui.misc.DBNComboBox;
import com.dci.intellij.dbn.common.util.Actions;
import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.data.sorting.SortingInstruction;
import com.dci.intellij.dbn.editor.data.state.sorting.action.ChangeSortingDirectionAction;
import com.dci.intellij.dbn.editor.data.state.sorting.action.DeleteSortingCriteriaAction;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatasetSortingColumnForm extends DBNFormBase {
    private JPanel actionsPanel;
    private JPanel mainPanel;
    private JLabel indexLabel;
    private JLabel dataTypeLabel;
    private DBNComboBox<DBColumn> columnComboBox;

    private final SortingInstruction sortingInstruction;

    DatasetSortingColumnForm(DatasetEditorSortingForm parent, @NotNull SortingInstruction sortingInstruction) {
        super(parent);
        this.sortingInstruction = sortingInstruction;

        DBDataset dataset = parent.getDataset();
        DBColumn column = sortingInstruction.getColumn(dataset);
        columnComboBox.setValues(dataset.getColumns());
        columnComboBox.setSelectedValue(column);
        columnComboBox.set(ValueSelectorOption.HIDE_DESCRIPTION, true);
        columnComboBox.setBackground(Colors.getTextFieldBackground());
        dataTypeLabel.setText(column.getDataType().getQualifiedName());
        dataTypeLabel.setForeground(UIUtil.getInactiveTextColor());

        ActionToolbar actionToolbar = Actions.createActionToolbar(
                actionsPanel,
                "DBNavigator.DataEditor.Sorting.Instruction", true,
                new ChangeSortingDirectionAction(this),
                new DeleteSortingCriteriaAction(this));
        actionsPanel.add(actionToolbar.getComponent(), BorderLayout.CENTER);
    }

    @NotNull
    public DatasetEditorSortingForm getParentForm() {
        return ensureParentComponent();
    }

    private class ColumnSelector extends ValueSelector<DBColumn>{
        ColumnSelector(DBColumn selectedColumn) {
            super(Icons.DBO_COLUMN_HIDDEN, "Select column...", selectedColumn, ValueSelectorOption.HIDE_DESCRIPTION);
            addListener((oldValue, newValue) -> {
                sortingInstruction.setColumnName(newValue.getName());
                dataTypeLabel.setText(newValue.getDataType().getQualifiedName());
            });
        }

        @Override
        public List<DBColumn> loadValues() {
            DBDataset dataset = getDataset();
            List<DBColumn> columns = new ArrayList<>(dataset.getColumns());
            Collections.sort(columns);
            return columns;
        }

        @Override
        public boolean isVisible(DBColumn value) {
            List<DatasetSortingColumnForm> sortingInstructionForms = getParentForm().getSortingInstructionForms();
            for (DatasetSortingColumnForm sortingColumnForm : sortingInstructionForms) {
                String columnName = sortingColumnForm.getSortingInstruction().getColumnName();
                if (Strings.equalsIgnoreCase(columnName, value.getName())) {
                    return false;
                }
            }
            return true;

        }
    }

    public void setIndex(int index) {
        sortingInstruction.setIndex(index);
        indexLabel.setText(Integer.toString(index));
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    public SortingInstruction getSortingInstruction() {
        return sortingInstruction;
    }

    public void remove() {
        getParentForm().removeSortingColumn(this);
    }

    @NotNull
    public DBDataset getDataset() {
        return getParentForm().getDataset();
    }
}
