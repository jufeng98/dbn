package com.dbn.data.type;

import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.Objects;

public class NumericDataTypeDefinition extends BasicDataTypeDefinition {
    private final Constructor constructor;

    @SneakyThrows
    public NumericDataTypeDefinition(String name, Class typeClass, int sqlType) {
        super(name, typeClass, sqlType, GenericDataType.NUMERIC);
        constructor = typeClass.getConstructor(String.class);
    }

    @Override
    @SneakyThrows
    public Object convert(@Nullable Object object) {
        if (object == null) return null;

        Number number = (Number) object;
        if (Objects.equals(object.getClass(), getTypeClass())) return object;

        return constructor.newInstance(number.toString());
    }
}
