package com.dbn.object.filter.custom;

import com.dbn.object.common.DBObject;
import com.dbn.object.type.DBObjectType;
import lombok.Getter;

import java.util.*;
import java.util.function.Function;

@Getter
final class ObjectFilterAttributesImpl<T extends DBObject> implements ObjectFilterAttributes<T> {

    private final DBObjectType objectType;
    private final Map<ObjectFilterAttribute, Function<T, Object>> attributes = new LinkedHashMap<>();

    public ObjectFilterAttributesImpl(DBObjectType objectType) {
        this.objectType = objectType;
    }

    @Override
    public Set<ObjectFilterAttribute> getAttributesTypes() {
        return attributes.keySet();
    }

    @Override
    public final Object getAttributeValue(T source, String attribute) {
        var valueProvider = attributes.get(attribute);

        return valueProvider == null ? null : valueProvider.apply(source);
    }

    public ObjectFilterAttributesImpl<T> withAttribute(Class type, String name, String description, Function<T, Object> valueProvider) {
        ObjectFilterAttribute attribute = new ObjectFilterAttribute(type, name, description);
        attributes.put(attribute, valueProvider);
        return this;
    }
}
