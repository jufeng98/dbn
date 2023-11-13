package com.dbn.object;

import com.dbn.data.type.DBDataType;
import com.dbn.data.type.DBNativeDataType;

import java.util.List;

public interface DBType extends DBProgram<DBTypeProcedure, DBTypeFunction, DBType> {
    List<DBTypeAttribute> getAttributes();
    DBType getSuperType();
    DBDataType getCollectionElementType();
    List<DBType> getTypes();

    DBNativeDataType getNativeDataType();
    boolean isCollection();

}