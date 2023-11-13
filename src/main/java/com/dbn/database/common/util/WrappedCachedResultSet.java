package com.dbn.database.common.util;

import org.jetbrains.annotations.Nullable;

public class WrappedCachedResultSet extends WrappedResultSet<CachedResultSet> {
    protected WrappedCachedResultSet(@Nullable CachedResultSet inner) {
        super(inner);
    }
}
