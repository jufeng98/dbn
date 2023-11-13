package com.dbn.data.type;

public class LiteralDataTypeDefinition extends BasicDataTypeDefinition {
    public LiteralDataTypeDefinition(String name, Class typeClass, int sqlType) {
        super(name, typeClass, sqlType, GenericDataType.LITERAL);
    }
}
