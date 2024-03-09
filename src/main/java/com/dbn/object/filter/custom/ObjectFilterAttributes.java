package com.dbn.object.filter.custom;

import com.dbn.object.common.DBObject;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface ObjectFilterAttributes<T extends DBObject> {
    Set<ObjectFilterAttribute> getAttributesTypes();
    Object getAttributeValue(T source, String attributeName);

    @NotNull
    static <T extends DBObject> ObjectFilterAttributes<T> of(DBObjectType objectType) {
        return ObjectFilterAttributeDefinitions.attributesOf(objectType);
    }
}
