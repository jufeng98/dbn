package com.dci.intellij.dbn.data.editor.ui.calendar;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/******************************************************
 *                  TableModels                       *
 ******************************************************/
class CalendarHeaderTableModel implements TableModel {
    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: return "S";
            case 1: return "M";
            case 2: return "T";
            case 3: return "W";
            case 4: return "T";
            case 5: return "F";
            case 6: return "S";
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }
}
