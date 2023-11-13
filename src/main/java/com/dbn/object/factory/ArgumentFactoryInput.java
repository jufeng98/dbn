package com.dbn.object.factory;

import com.dbn.common.util.Strings;
import com.dbn.object.type.DBObjectType;
import lombok.Getter;

import java.util.List;


@Getter
public class ArgumentFactoryInput extends ObjectFactoryInput{
    private final String dataType;
    private final boolean input;
    private final boolean output;

    public ArgumentFactoryInput(ObjectFactoryInput parent, int index, String objectName, String dataType, boolean input, boolean output) {
        super(objectName, DBObjectType.ARGUMENT, parent, index);
        this.dataType = dataType == null ? "" : dataType.trim();
        this.input = input;
        this.output = output;
    }

    @Override
    public void validate(List<String> errors) {
        String objectName = getObjectName();
        if (objectName.length() == 0) {
            errors.add("argument name is not specified at index " + getIndex());

        } else if (!Strings.isWord(objectName)) {
            errors.add("invalid argument name specified at index " + getIndex() + ": \"" + objectName + "\"");
        }

        if (dataType.length() == 0){
            if (objectName.length() > 0) {
                errors.add("missing data type for argument \"" + objectName + "\"");
            } else {
                errors.add("missing data type for argument at index " + getIndex());
            }
        }
    }
}
