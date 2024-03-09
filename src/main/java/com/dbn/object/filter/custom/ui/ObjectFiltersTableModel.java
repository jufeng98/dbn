package com.dbn.object.filter.custom.ui;

import com.dbn.common.ui.table.DBNEditableTableModel;
import com.dbn.object.filter.custom.ObjectFilter;
import com.dbn.object.filter.custom.ObjectCustomFilterSettings;
import com.dbn.object.type.DBObjectType;
import lombok.Getter;

import java.util.List;

@Getter
public class ObjectFiltersTableModel extends DBNEditableTableModel {
    private final ObjectCustomFilterSettings settings;

    ObjectFiltersTableModel(ObjectCustomFilterSettings settings) {
        this.settings = settings;
    }

    @Override
    public int getRowCount() {
        return getFilters().size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnIndex == 0 ? "Object Type" :
               columnIndex == 1 ? "Filter Expression" :
               columnIndex == 2 ? "Enabled" : null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? DBObjectType.class :
            columnIndex == 1 ? String.class :
            columnIndex == 2 ? Boolean.class : String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ObjectFilter filter = getFilter(rowIndex);
        return
           columnIndex == 0 ? filter.getObjectType() :
           columnIndex == 1 ? filter.getExpression() :
           columnIndex == 2 ? filter.isEnabled() : null;
    }

    @Override
    public void setValueAt(Object o, int rowIndex, int columnIndex) {
/*        Object actualValue = getValueAt(rowIndex, columnIndex);
        if (!Commons.match(actualValue, o)) {
            EnvironmentType environmentType = environmentTypes.get(rowIndex);
            if (columnIndex == 0) {
                environmentType.setName((String) o);
            } else if (columnIndex == 1) {
                environmentType.setDescription((String) o);
            } else if (columnIndex == 2) {
                environmentType.setReadonlyData((Boolean) o);
            } else if (columnIndex == 3) {
                environmentType.setReadonlyCode((Boolean) o);
            } else if (columnIndex == 4) {
                Color color = (Color) o;
                environmentType.setColor(color);
            }

            notifyListeners(rowIndex, rowIndex, columnIndex);
        }*/
    }

    private ObjectFilter getFilter(int rowIndex) {
        return getFilters().get(rowIndex);
    }

    @Override
    public void insertRow(int rowIndex) {
        List<ObjectFilter> filters = getFilters();
        filters.add(rowIndex, new ObjectFilter(settings));
        notifyListeners(rowIndex, filters.size()-1, -1);
    }

    private List<ObjectFilter> getFilters() {
        return settings.getFilters();
    }

    @Override
    public void removeRow(int rowIndex) {
        List<ObjectFilter> filters = getFilters();
        if (filters.size() > rowIndex) {
            filters.remove(rowIndex);
            notifyListeners(rowIndex, filters.size()-1, -1);
        }
    }
}
