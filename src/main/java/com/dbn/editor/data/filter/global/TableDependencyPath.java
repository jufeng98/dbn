package com.dbn.editor.data.filter.global;

import com.dbn.object.DBTable;

import java.util.ArrayList;
import java.util.List;

public class TableDependencyPath {
    private final List<DBTable> tables = new ArrayList<>();

    @Override
    public TableDependencyPath clone() {
        TableDependencyPath clone = new TableDependencyPath();
        clone.tables.addAll(tables);
        return clone;
    }

    public void addTable(DBTable table) {
        tables.add(table);
    }

   public boolean isRecursive(DBTable table) {
        return tables.contains(table);
    }

    public static TableDependencyPath buildDependencyPath(TableDependencyPath path, DBTable sourceTable, DBTable targetTable) {
        if (path == null) {
            path = new TableDependencyPath();
        } else {
            if (path.isRecursive(sourceTable)) {
                return null;
            } else {
                path = path.clone();
            }
        }

        path.addTable(sourceTable);
        return null;


    }

}
