package com.dbn.object.factory;

import com.dbn.common.util.Strings;
import com.dbn.object.DBSchema;
import com.dbn.object.type.DBObjectType;
import com.dbn.object.lookup.DBObjectRef;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class MethodFactoryInput extends ObjectFactoryInput{
    private List<ArgumentFactoryInput> arguments = new ArrayList<>();
    private ArgumentFactoryInput returnArgument;
    private final DBObjectRef<DBSchema> schema;

    public MethodFactoryInput(DBSchema schema, String objectName, DBObjectType methodType, int index) {
        super(objectName, methodType, null, index);
        this.schema = DBObjectRef.of(schema);
    }

    public DBSchema getSchema() {
        return DBObjectRef.get(schema);
    }

    public boolean isFunction() {
        return returnArgument != null;
    }

    @Override
    public void validate(List<String> errors) {
        String objectName = getObjectName();
        if (objectName.length() == 0) {
            String hint = getParent() == null ? "" : " at index " + getIndex();
            errors.add(getObjectType().getName() + " name is not specified" + hint);
            
        } else if (!Strings.isWord(objectName)) {
            errors.add("invalid " + getObjectType().getName() +" name specified" + ": \"" + objectName + "\"");
        }


        if (returnArgument != null) {
            if (returnArgument.getDataType().length() == 0)
                errors.add("missing data type for return argument");
        }

        Set<String> argumentNames = new HashSet<>();
        for (ArgumentFactoryInput argument : arguments) {
            argument.validate(errors);
            String argumentName = argument.getObjectName();
            if (argumentNames.contains(argumentName)) {
                String hint = getParent() == null ? "" : " for " + getObjectType().getName() + " \"" + objectName + "\"";
                errors.add("duplicate argument name \"" + argumentName + "\"" + hint);
            }
            argumentNames.add(argumentName);
        }
    }
}
