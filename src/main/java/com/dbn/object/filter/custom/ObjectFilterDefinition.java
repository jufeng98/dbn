package com.dbn.object.filter.custom;

import com.dbn.object.common.DBObject;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ObjectFilterDefinition<T extends DBObject> {
    List<ObjectFilterAttribute> getAttributes();
    Object getAttributeValue(T source, String attributeName);

    @NotNull
    static <T extends DBObject> ObjectFilterDefinition<T> of(DBObjectType objectType) {
        return ObjectFilterAttributeDefinitions.attributesOf(objectType);
    }
}
