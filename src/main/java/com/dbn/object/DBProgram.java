package com.dbn.object;

import com.dbn.object.common.DBSchemaObject;

import java.util.List;

public interface DBProgram<P extends DBProcedure, F extends DBFunction, T extends DBType> extends DBSchemaObject {
    List<P> getProcedures();
    List<F> getFunctions();
    List<T> getTypes();

    F getFunction(String name, short overload);
    P getProcedure(String name, short overload);
    T getType(String name);

    DBMethod getMethod(String name, short overload);
    boolean isEmbedded();
}
