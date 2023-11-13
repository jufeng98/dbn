package com.dbn.object.factory.ui;

import com.dbn.common.ui.component.DBNComponent;
import com.dbn.object.DBSchema;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;

public class ProcedureFactoryInputForm extends MethodFactoryInputForm {

    public ProcedureFactoryInputForm(@NotNull DBNComponent parent, DBSchema schema, DBObjectType objectType, int index) {
        super(parent, schema, objectType, index);
    }

    @Override
    public boolean hasReturnArgument() {
        return false;
    }
}