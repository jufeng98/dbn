package com.dbn.data.type;

import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Objects;

public class DateTimeDataTypeDefinition extends BasicDataTypeDefinition {
    private final Constructor constructor;

    @SneakyThrows
    public DateTimeDataTypeDefinition(String name, Class typeClass, int sqlType) {
        super(name, typeClass, sqlType, GenericDataType.DATE_TIME);
        constructor = typeClass.getConstructor(long.class);
    }

    @Override
    @SneakyThrows
    public Object convert(@Nullable Object object) {
        if (object == null) return null;

        Date date = (Date) object;
        if (Objects.equals(object.getClass(), getTypeClass())) return object;

        return constructor.newInstance(date.getTime());
    }
}