package com.dbn.data.type;

import com.dbn.database.common.util.DataTypeParseAdapter;
import org.jetbrains.annotations.Nullable;

public interface DataTypeDefinition {
    @Nullable
    <T> DataTypeParseAdapter<T> getParseAdapter();

    String getName();
    Class getTypeClass();
    int getSqlType();

    boolean isPseudoNative();

    GenericDataType getGenericDataType();
    Object convert(@Nullable Object object);
    @Nullable String getContentTypeName();
}
