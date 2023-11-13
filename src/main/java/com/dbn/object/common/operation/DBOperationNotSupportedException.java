package com.dbn.object.common.operation;

import com.dbn.object.type.DBObjectType;

public class DBOperationNotSupportedException extends UnsupportedOperationException{
    public DBOperationNotSupportedException(DBOperationType operationType, DBObjectType objectType) {
        super( "Operation " + operationType.getName() + " not supported for " + objectType.getListName());
    }

    public DBOperationNotSupportedException(DBOperationType operationType) {
        super( "Operation " + operationType.getName() + " not supported");
    }
}
