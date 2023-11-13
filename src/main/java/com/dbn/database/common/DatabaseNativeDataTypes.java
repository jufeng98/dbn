package com.dbn.database.common;

import com.dbn.data.type.*;
import com.dbn.database.common.util.DataTypeParseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class DatabaseNativeDataTypes {
    protected List<DataTypeDefinition> dataTypes = new ArrayList<>();
    public List<DataTypeDefinition> list() {return dataTypes;}

    protected void createBasicDefinition(String name, Class typeClass, int sqlType, GenericDataType genericDataType) {
        createBasicDefinition(name, typeClass, sqlType, genericDataType, false, null);
    }

    protected void createBasicDefinition(String name, Class typeClass, int sqlType, GenericDataType genericDataType, boolean pseudoNative, String contentTypeName) {
        BasicDataTypeDefinition dataTypeDefinition = new BasicDataTypeDefinition(name, typeClass, sqlType, genericDataType, pseudoNative, contentTypeName);
        dataTypes.add(dataTypeDefinition);
    }

    protected void createLargeValueDefinition(String name, Class typeClass, int sqlType, GenericDataType genericDataType, boolean pseudoNative, String contentTypeName) {
        LargeObjectDataTypeDefinition dataTypeDefinition = new LargeObjectDataTypeDefinition(name, typeClass, sqlType, genericDataType, pseudoNative, contentTypeName);
        dataTypes.add(dataTypeDefinition);
    }

    protected void createLargeValueDefinition(String name, Class typeClass, int sqlType, GenericDataType genericDataType) {
        LargeObjectDataTypeDefinition dataTypeDefinition = new LargeObjectDataTypeDefinition(name, typeClass, sqlType, genericDataType);
        dataTypes.add(dataTypeDefinition);
    }

    protected void createLiteralDefinition(String name, Class typeClass, int sqlType) {
        BasicDataTypeDefinition dataTypeDefinition = new LiteralDataTypeDefinition(name, typeClass, sqlType);
        dataTypes.add(dataTypeDefinition);
    }

    protected void createNumericDefinition(String name, Class typeClass, int sqlType) {
        BasicDataTypeDefinition dataTypeDefinition = new NumericDataTypeDefinition(name, typeClass, sqlType);
        dataTypes.add(dataTypeDefinition);
    }

    protected <T> void createDateTimeDefinition(String name, Class<T> typeClass, int sqlType) {
        createDateTimeDefinition(name, typeClass, sqlType, null);
    }

    protected <T> void createDateTimeDefinition(String name, Class<T> typeClass, int sqlType, DataTypeParseAdapter<T> parseAdapter) {
        BasicDataTypeDefinition dataTypeDefinition = new DateTimeDataTypeDefinition(name, typeClass, sqlType);
        dataTypeDefinition.setParseAdapter(parseAdapter);
        dataTypes.add(dataTypeDefinition);

    }


}
