package com.dbn.data.type;

import org.jetbrains.annotations.Nullable;

public class LargeObjectDataTypeDefinition extends BasicDataTypeDefinition {
    public LargeObjectDataTypeDefinition(String name, Class typeClass, int sqlType, GenericDataType genericDataType, boolean pseudoNative, String contentTypeName) {
        super(name, typeClass, sqlType, genericDataType, pseudoNative, contentTypeName);
    }

    public LargeObjectDataTypeDefinition(String name, Class typeClass, int sqlType, GenericDataType genericDataType) {
        super(name, typeClass, sqlType, genericDataType);
    }

    @Override
    public Object convert(@Nullable Object object) {
        return object;
    }
}
