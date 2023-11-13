package com.dbn.object.factory.ui;

import com.dbn.common.ui.component.DBNComponent;
import com.dbn.data.type.ui.DataTypeEditor;
import com.dbn.object.DBSchema;
import com.dbn.object.type.DBObjectType;
import com.dbn.object.factory.ArgumentFactoryInput;
import com.dbn.object.factory.MethodFactoryInput;
import com.dbn.object.factory.ObjectFactoryInput;
import org.jetbrains.annotations.NotNull;

public class FunctionFactoryInputForm extends MethodFactoryInputForm {

    public FunctionFactoryInputForm(@NotNull DBNComponent parent, DBSchema schema, DBObjectType objectType, int index) {
        super(parent, schema, objectType, index);
    }

    @Override
    public MethodFactoryInput createFactoryInput(ObjectFactoryInput parent) {
        MethodFactoryInput methodFactoryInput = super.createFactoryInput(parent);

        DataTypeEditor returnTypeEditor = (DataTypeEditor) returnArgumentDataTypeEditor;

        ArgumentFactoryInput returnArgument = new ArgumentFactoryInput(
                methodFactoryInput, 0, "return",
                returnTypeEditor.getDataTypeRepresentation(),
                false, true);

        methodFactoryInput.setReturnArgument(returnArgument);
        return methodFactoryInput;
    }

    @Override
    public boolean hasReturnArgument() {
        return true;
    }
}
