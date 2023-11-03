package com.dci.intellij.dbn.data.value;

import com.dci.intellij.dbn.data.type.GenericDataType;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.EnumMap;
import java.util.Map;

import static com.dci.intellij.dbn.common.util.Unsafe.cast;
import static com.dci.intellij.dbn.diagnostics.Diagnostics.conditionallyLog;

@Slf4j
public abstract class ValueAdapter<T> {

    public abstract GenericDataType getGenericDataType();
    public abstract @Nullable T read() throws SQLException;
    public abstract @Nullable String export() throws SQLException;
    public abstract void write(Connection connection, PreparedStatement preparedStatement, int parameterIndex, @Nullable T value) throws SQLException;
    public abstract void write(Connection connection, ResultSet resultSet, int columnIndex, @Nullable T value) throws SQLException;
    public abstract String getDisplayValue();

    public static final Map<GenericDataType, Class<? extends ValueAdapter<?>>> REGISTRY = new EnumMap<>(GenericDataType.class);
    static {
        REGISTRY.put(GenericDataType.ARRAY, ArrayValue.class);
        REGISTRY.put(GenericDataType.BLOB, BlobValue.class);
        REGISTRY.put(GenericDataType.CLOB, ClobValue.class);
        REGISTRY.put(GenericDataType.NCLOB, NClobValue.class);
        REGISTRY.put(GenericDataType.XMLTYPE, XmlTypeValue.class);
    }

    public static boolean supports(GenericDataType genericDataType) {
        return REGISTRY.containsKey(genericDataType);
    }

    private static <T> Class<ValueAdapter<T>> get(GenericDataType genericDataType) {
        return cast(REGISTRY.get(genericDataType));
    }

    public static <T> ValueAdapter<T> create(GenericDataType genericDataType) throws SQLException {
        try {
            Class<ValueAdapter<T>> valueAdapterClass = get(genericDataType);
            return valueAdapterClass.getDeclaredConstructor().newInstance();
        } catch (Throwable e) {
            conditionallyLog(e);
            handleException(e, genericDataType);
        }
        return null;
    }

    public static <T> ValueAdapter<T> create(GenericDataType genericDataType, ResultSet resultSet, int columnIndex) throws SQLException {
        try {
            Class<ValueAdapter<T>> valueAdapterClass = get(genericDataType);
            Constructor<ValueAdapter<T>> constructor = valueAdapterClass.getConstructor(ResultSet.class, int.class);
            return constructor.newInstance(resultSet, columnIndex);
        } catch (Throwable e) {
            conditionallyLog(e);
            handleException(e, genericDataType);
        }
        return null;
    }

    public static <T> ValueAdapter<T> create(GenericDataType genericDataType, CallableStatement callableStatement, int parameterIndex) throws SQLException {
        Class<ValueAdapter<T>> valueAdapterClass = get(genericDataType);
        try {
            Constructor<ValueAdapter<T>> constructor = valueAdapterClass.getConstructor(CallableStatement.class, int.class);
            return constructor.newInstance(callableStatement, parameterIndex);
        } catch (Throwable e) {
            conditionallyLog(e);
            handleException(e, genericDataType);
            return null;
        }
    }

    private static void handleException(Throwable e, GenericDataType genericDataType) throws SQLException {
        if (e instanceof InvocationTargetException) {
            InvocationTargetException invocationTargetException = (InvocationTargetException) e;
            e = invocationTargetException.getTargetException();
        }
        if (e instanceof SQLException) {
            throw (SQLException) e;
        } else {
            log.error("Error creating value adapter for generic type " + genericDataType.name() + '.', e);
            throw new SQLException("Error creating value adapter for generic type " + genericDataType.name() + '.', e);
        }
    }
}
