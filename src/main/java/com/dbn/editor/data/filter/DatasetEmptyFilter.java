package com.dbn.editor.data.filter;

import com.dbn.common.icon.Icons;
import com.dbn.connection.ConnectionId;
import com.dbn.data.sorting.SortingState;
import com.dbn.object.DBDataset;
import com.intellij.openapi.options.ConfigurationException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DatasetEmptyFilter implements DatasetFilter{

    @Override
    public Icon getIcon() {
        return Icons.DATASET_FILTER_EMPTY;
    }

    @Override
    @NotNull
    public String getId() {
        return "EMPTY_FILTER";
    }

    @Override
    public String getName() {
        return "No Filter";
    }

    @Override
    public String getVolatileName() {
        return getName();
    }

    @Override
    public String createSelectStatement(DBDataset dataset, SortingState sortingState) {
        setError(null);
        StringBuilder buffer = new StringBuilder();
        DatasetFilterUtil.createSimpleSelectStatement(dataset, buffer);
        DatasetFilterUtil.addOrderByClause(dataset, buffer, sortingState);
        return buffer.toString();
    }

    @Override
    public ConnectionId getConnectionId() { return null; }
    @Override
    public String getDatasetName() { return null; }

    @Override
    public boolean isPersisted() {
        return true;
    }

    @Override
    public boolean isTemporary() {
        return false;
    }

    @Override
    public boolean isIgnored() {
        return false;
    }

    @Override
    public DatasetFilterType getFilterType() {
        return DatasetFilterType.NONE;
    }

    @Override
    public String getError() { return null; }
    @Override
    public void setError(String error) {}
    @Override
    public DatasetFilterGroup getFilterGroup() { return null; }
    @Override
    public JComponent createComponent() { return null; }
    @Override
    public boolean isModified() { return false; }
    @Override
    public void apply() throws ConfigurationException {}
    @Override
    public void reset() {}
    @Override
    public void disposeUIResources() {}

    @Override
    public void readConfiguration(Element element) {}
    @Override
    public void writeConfiguration(Element element) {}
}
