package com.dbn.editor.data.filter.global;

import com.dbn.connection.ConnectionHandler;
import com.dbn.object.DBColumn;
import com.dbn.object.DBDataset;
import com.dbn.object.DBTable;

public class GlobalDataFilter implements SelectStatementFilter{
    
    private DBTable filterTable;
    private DBColumn filterColumn;
    private Object filterValue;

    public GlobalDataFilter(DBColumn filterColumn, Object filterValue) {
        this.filterColumn = filterColumn;
        this.filterValue = filterValue;
        this.filterTable = (DBTable) filterColumn.getDataset();
    }

    public ConnectionHandler getConnection() {
        return filterColumn.getConnection();
    }

    public DBDataset getFilterTable() {
        return filterTable;
    }

    public DBColumn getFilterColumn() {
        return filterColumn;
    }

    public Object getFilterValue() {
        return filterValue;
    }

    @Override
    public String createSelectStatement(DBDataset dataset) {
        return null;
    }

    private void buildDependencyLink(DBTable fromTable, DBTable toTable) {
        for (DBColumn column : fromTable.getForeignKeyColumns()) {
            
        }
    }
}
