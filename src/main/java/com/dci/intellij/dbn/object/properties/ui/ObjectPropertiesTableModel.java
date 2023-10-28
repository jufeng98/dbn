package com.dci.intellij.dbn.object.properties.ui;

import com.dci.intellij.dbn.common.dispose.StatefulDisposableBase;
import com.dci.intellij.dbn.common.ui.table.DBNReadonlyTableModel;
import com.dci.intellij.dbn.object.properties.PresentableProperty;

import java.util.ArrayList;
import java.util.List;

public class ObjectPropertiesTableModel extends StatefulDisposableBase implements DBNReadonlyTableModel {
    private List<PresentableProperty> presentableProperties = new ArrayList<>();

    ObjectPropertiesTableModel() {}

    ObjectPropertiesTableModel(List<PresentableProperty> presentableProperties) {
        this.presentableProperties = presentableProperties;
    }

    @Override
    public int getRowCount() {
        return presentableProperties.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return
            columnIndex == 0 ? "Property" :
            columnIndex == 1 ? "Value" : null;
    }

    @Override public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }
    @Override public Object getValueAt(int rowIndex, int columnIndex) {
        return presentableProperties.get(rowIndex);
    }

    @Override
    public void disposeInner() {
        nullify();
    }
}
