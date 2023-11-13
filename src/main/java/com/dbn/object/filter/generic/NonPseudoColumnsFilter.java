package com.dbn.object.filter.generic;

import com.dbn.common.filter.Filter;
import com.dbn.object.DBColumn;

public class NonPseudoColumnsFilter implements Filter<DBColumn> {
    public static final Filter<DBColumn> INSTANCE = new NonPseudoColumnsFilter();

    private NonPseudoColumnsFilter() {}

    @Override
    public boolean accepts(DBColumn column) {
        return !column.isHidden();
    }
}
