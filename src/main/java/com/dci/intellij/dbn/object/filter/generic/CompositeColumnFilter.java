package com.dci.intellij.dbn.object.filter.generic;

import com.dci.intellij.dbn.common.filter.Filter;
import com.dci.intellij.dbn.object.DBColumn;
import org.jetbrains.annotations.Nullable;


public class CompositeColumnFilter implements Filter<DBColumn> {
    public static final CompositeColumnFilter INSTANCE = new CompositeColumnFilter();
    private enum Signature {
        AUDIT_AND_PSEUDO,
        AUDIT,
        PSEUDO,
        NONE
    }

    private CompositeColumnFilter() {}

    @Nullable
    public static Filter<DBColumn> get(boolean pseudo, boolean audit) {
        if (pseudo && audit) return INSTANCE;
        if (pseudo) return NonPseudoColumnsFilter.INSTANCE;
        if (audit) return NonAuditColumnsFilter.INSTANCE;
        return null;
    }

    public static Object signature(boolean pseudo, boolean audit) {
        if (pseudo && audit) return Signature.AUDIT_AND_PSEUDO;
        if (pseudo) return Signature.PSEUDO;
        if (audit) return Signature.AUDIT;
        return Signature.NONE;
    }

    @Override
    public boolean accepts(DBColumn object) {
        return NonPseudoColumnsFilter.INSTANCE.accepts(object)
                && NonAuditColumnsFilter.INSTANCE.accepts(object);
    }
}
