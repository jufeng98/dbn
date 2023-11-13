package com.dbn.execution.method.history.ui;

import com.dbn.object.DBMethod;
import com.dbn.object.DBProgram;
import com.dbn.object.lookup.DBObjectRef;
import com.dbn.object.type.DBObjectType;

@Deprecated
public class MethodRefUtil {
    public static DBProgram getProgram(DBObjectRef<DBMethod> methodRef) {
        return (DBProgram) methodRef.getParentObject(DBObjectType.PROGRAM);
    }

    public static String getProgramName(DBObjectRef<DBMethod> methodRef) {
        DBObjectRef programRef = methodRef.getParentRef(DBObjectType.PROGRAM);
        return programRef == null ? null : programRef.getObjectName();
    }

    public static DBObjectType getProgramObjectType(DBObjectRef<DBMethod> methodRef) {
        DBObjectRef programRef = methodRef.getParentRef(DBObjectType.PROGRAM);
        return programRef == null ? null : programRef.getObjectType();
    }
}
