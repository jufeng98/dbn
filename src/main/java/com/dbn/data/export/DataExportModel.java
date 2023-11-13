package com.dbn.data.export;

import com.dbn.data.type.GenericDataType;
import com.intellij.openapi.project.Project;

import java.util.List;

public interface DataExportModel {
    String getTableName();
    int getColumnCount();
    int getRowCount();
    Object getValue(int rowIndex, int columnIndex);
    String getColumnName(int columnIndex);
    String getColumnFriendlyName(int columnIndex);
    GenericDataType getGenericDataType(int columnIndex);
    Project getProject();
    List<String> getWarnings();

    void addWarning(String warning);
}
