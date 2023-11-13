package com.dbn.object.filter.generic;

import com.dbn.common.filter.Filter;
import com.dbn.object.DBSchema;

public class NonEmptySchemaFilter implements Filter<DBSchema> {
    public static final Filter<DBSchema> INSTANCE = new NonEmptySchemaFilter();

    @Override
    public boolean accepts(DBSchema schema) {
        return !schema.isEmptySchema();
    }
}
