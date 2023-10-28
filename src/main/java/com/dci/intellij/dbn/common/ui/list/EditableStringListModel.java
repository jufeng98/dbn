package com.dci.intellij.dbn.common.ui.list;

import com.dci.intellij.dbn.common.ui.table.DBNEditableTableModel;
import lombok.Getter;

import java.util.*;

import static com.dci.intellij.dbn.common.util.Lists.isOutOfBounds;

@Getter
public class EditableStringListModel extends DBNEditableTableModel {
    private final List<String> originalData;
    private final List<String> data;

    public EditableStringListModel(Collection<String> data, boolean sorted) {
        this.originalData = new ArrayList<>(data);
        this.data = new ArrayList<>(data);
        if (sorted) Collections.sort(this.data);
    }

    public boolean isChanged() {
        return !originalData.equals(data);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return "DATA";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rowIndex < data.size() ? data.get(rowIndex) : null;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (isOutOfBounds(data, rowIndex)) return;

        String currentValue = data.get(rowIndex);
        if (Objects.equals(currentValue, value)) return;

        data.set(rowIndex, (String) value);
        notifyListeners(rowIndex, rowIndex, columnIndex);
    }

    @Override
    public void insertRow(int rowIndex) {
        data.add(rowIndex, "");
        notifyListeners(rowIndex, data.size() + 1, -1);
    }

    @Override
    public void removeRow(int rowIndex) {
        if (isOutOfBounds(data, rowIndex)) return;
        if (rowIndex <= -1 || rowIndex >= data.size()) return;

        data.remove(rowIndex);
        notifyListeners(rowIndex, data.size() + 1, -1);
    }
}
