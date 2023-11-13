package com.dbn.object;

import com.dbn.language.common.DBLanguage;
import com.dbn.object.common.DBSchemaObject;
import com.dbn.object.type.DBMethodType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DBMethod extends DBSchemaObject, DBOrderedObject {
    List<DBArgument> getArguments();
    DBArgument getArgument(String name);
    DBArgument getReturnArgument();
    DBProgram getProgram();
    DBMethodType getMethodType();

    short getPosition();

    boolean isProgramMethod();
    boolean isDeterministic();
    boolean hasDeclaredArguments();
    @NotNull
    DBLanguage getLanguage();
}
