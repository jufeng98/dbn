package com.dbn.data.type;

import com.dbn.database.common.util.DataTypeParseAdapter;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class BasicDataTypeDefinition implements DataTypeDefinition {
    private final GenericDataType genericDataType;
    private final String name;
    private final Class typeClass;
    private final int sqlType;
    private final boolean pseudoNative;
    private final String contentTypeName;
    private DataTypeParseAdapter parseAdapter;


    public BasicDataTypeDefinition(String name, Class typeClass, int sqlType, GenericDataType genericDataType) {
        this(name, typeClass, sqlType, genericDataType, false);
    }

    public BasicDataTypeDefinition(String name, Class typeClass, int sqlType, GenericDataType genericDataType, boolean pseudoNative) {
        this(name, typeClass, sqlType, genericDataType, pseudoNative, null);
    }

    public BasicDataTypeDefinition(String name, Class typeClass, int sqlType, GenericDataType genericDataType, boolean pseudoNative, String contentTypeName) {
        this.name = name;
        this.typeClass = typeClass;
        this.sqlType = sqlType;
        this.genericDataType = genericDataType;
        this.pseudoNative = pseudoNative;
        this.contentTypeName = contentTypeName;
    }

    @Override
    public String toString() {
        return "[NAME = " + name + ", " +
                "GENERIC_TYPE = " + genericDataType + ", " +
                "TYPE_CLASS = " + typeClass.getName() + ", " +
                "SQL_TYPE = " + sqlType + ']';
    }

    @Override
    public Object convert(@Nullable Object object) {
        return object;
    }
}