package com.dbn.data.model.basic;

import com.dbn.common.ui.table.DBNTableGutterModel;

public class BasicDataGutterModel extends DBNTableGutterModel<BasicDataModel> {
    public BasicDataGutterModel(BasicDataModel dataModel) {
        super(dataModel);
    }

    @Override
    public Object getElementAt(int index) {
        return getTableModel().getRowAtIndex(index);
    }
}
