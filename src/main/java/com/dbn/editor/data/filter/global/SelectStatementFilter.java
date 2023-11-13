package com.dbn.editor.data.filter.global;

import com.dbn.object.DBDataset;

public interface SelectStatementFilter {
    String createSelectStatement(DBDataset dataset);
}
