package com.dbn.object.factory;

import com.dbn.object.type.DBObjectType;
import lombok.Getter;

import java.util.List;

@Getter
public abstract class ObjectFactoryInput {
    private final String objectName;
    private final DBObjectType objectType;
    private final ObjectFactoryInput parent;
    private final int index;

    protected ObjectFactoryInput(String objectName, DBObjectType objectType, ObjectFactoryInput parent, int index) {
        this.objectName = objectName == null ? "" : objectName.trim();
        this.objectType = objectType;
        this.parent = parent;
        this.index = index;
    }

    public abstract void validate(List<String> errors);
}
