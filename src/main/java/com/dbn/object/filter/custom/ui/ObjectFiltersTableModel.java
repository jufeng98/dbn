package com.dbn.object.filter.custom.ui;

import com.dbn.common.ui.table.DBNEditableTableModel;
import com.dbn.common.util.Commons;
import com.dbn.object.filter.custom.ObjectFilter;
import com.dbn.object.filter.custom.ObjectFilterSettings;
import com.dbn.object.type.DBObjectType;
import lombok.Getter;

import java.util.List;

import static com.dbn.common.util.Lists.convert;
import static com.dbn.common.util.Lists.first;

@Getter
public class ObjectFiltersTableModel extends DBNEditableTableModel {
    private final ObjectFilterSettings settings;
    private final List<ObjectFilter<?>> filters;

    ObjectFiltersTableModel(ObjectFilterSettings settings) {
        this.settings = settings;
        this.filters = settings.getFilters();
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
        return columnIndex == 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ObjectFilter<?> filter = getFilter(rowIndex);
        return
           columnIndex == 0 ? filter.getObjectType() :
           columnIndex == 1 ? filter.getExpression() :
           columnIndex == 2 ? filter.isEnabled() : null;
    }

    @Override
    public void setValueAt(Object o, int rowIndex, int columnIndex) {
        Object actualValue = getValueAt(rowIndex, columnIndex);
        if (!Commons.match(actualValue, o)) {
            ObjectFilter<?> filter = getFilter(rowIndex);
            if (columnIndex == 2) {
                filter.setEnabled((Boolean) o);
                filter.getSettings().setModified(true);
            }
            notifyListeners(rowIndex, rowIndex, columnIndex);
        }
    }

    private ObjectFilter<?> getFilter(int rowIndex) {
        return filters.get(rowIndex);
    }

    @Override
    public void insertRow(int rowIndex) {
        checkRowBounds(rowIndex);
        filters.add(rowIndex, new ObjectFilter<>(settings));
        notifyListeners(rowIndex, filters.size()-1, -1);
    }

    @Override
    public void removeRow(int rowIndex) {
        checkRowBounds(rowIndex);
        filters.remove(rowIndex);
        notifyListeners(rowIndex, filters.size()-1, -1);
    }

    public List<DBObjectType> getFilterObjectTypes() {
        return convert(filters, f -> f.getObjectType());
    }

    public void createOrUpdate(ObjectFilter<?> filter) {
        ObjectFilter<?> currentFilter = first(filters, f -> f.getObjectType() == filter.getObjectType());
        if (currentFilter == null) {
            filters.add(filter);
        } else {
            currentFilter.setExpression(filter.getExpression());
            currentFilter.setEnabled(filter.isEnabled());
        }
        int rowIndex = filters.indexOf(filter);
        notifyListeners(0, rowIndex, -1);
    }
}
