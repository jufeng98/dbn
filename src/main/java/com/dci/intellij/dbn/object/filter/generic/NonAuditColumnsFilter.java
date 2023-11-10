package com.dci.intellij.dbn.object.filter.generic;

import com.dci.intellij.dbn.common.filter.Filter;
import com.dci.intellij.dbn.object.DBColumn;

public class NonAuditColumnsFilter implements Filter<DBColumn> {
    public static final Filter<DBColumn> INSTANCE = new NonAuditColumnsFilter();

    private NonAuditColumnsFilter() {}

    @Override
    public boolean accepts(DBColumn column) {
        return !column.isAudit();
    }
}
