package com.dbn.editor.data.filter.global;

import com.dbn.common.util.Cloneable;
import com.dbn.object.DBColumn;

import java.util.ArrayList;
import java.util.List;

public class DataDependencyPath implements Cloneable<DataDependencyPath> {
    private final List<DBColumn> pathElements = new ArrayList<>();

    private DataDependencyPath() {}

    public DataDependencyPath(DBColumn startElement) {
        addPathElement(startElement);
    }

    public void addPathElement(DBColumn pathElement) {
        pathElements.add(pathElement);
    }

    public boolean isRecursiveElement(DBColumn element) {
        for (DBColumn pathElement : pathElements ) {
            if (pathElement.getDataset() == element.getDataset()) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return pathElements.size();
    }

    @Override
    public DataDependencyPath clone() {
        DataDependencyPath clone = new DataDependencyPath();
        clone.pathElements.addAll(pathElements);
        return clone; 
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        DBColumn lastElement = pathElements.get(pathElements.size() - 1);
        for (DBColumn pathElement : pathElements) {
            buffer.append(pathElement.getDataset().getName());
            buffer.append(".");
            buffer.append(pathElement.getName());
            if (lastElement != pathElement) {
                buffer.append(" > ");
            }
        }
        return buffer.toString();
    }
}
