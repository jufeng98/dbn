package com.dbn.editor.data.filter;

import com.dbn.common.icon.Icons;
import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.data.sorting.SortingState;
import com.dbn.editor.data.filter.ui.DatasetCustomFilterForm;
import com.dbn.object.DBDataset;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DatasetConsoleFilter extends DatasetFilterImpl {
    private String rawSql;

    protected DatasetConsoleFilter(DatasetFilterGroup parent, String name) {
        super(parent, name, DatasetFilterType.CUSTOM);
    }

    @Override
    public void generateName() {
    }

    @Override
    public String getVolatileName() {
        ConfigurationEditorForm<?> configurationEditorForm = getSettingsEditor();
        if (configurationEditorForm != null) {
            DatasetCustomFilterForm customFilterForm = (DatasetCustomFilterForm) configurationEditorForm;
            return customFilterForm.getFilterName();
        }
        return super.getDisplayName();
    }

    @Override
    public boolean isIgnored() {
        return false;
    }

    @Override
    public Icon getIcon() {
        return getError() == null ?
                Icons.DATASET_FILTER_CUSTOM :
                Icons.DATASET_FILTER_CUSTOM_ERR;
    }

    @Override
    public String createSelectStatement(DBDataset dataset, SortingState sortingState, Integer pageNum, Integer pageSize) {
        setError(null);

        return DatasetFilterUtil.appendLimitIfLack(rawSql, pageNum, pageSize, dataset.getProject(),
                dataset.getConnection().getDatabaseType());
    }
}
