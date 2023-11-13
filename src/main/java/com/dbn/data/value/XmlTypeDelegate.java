package com.dbn.data.value;

import com.dbn.connection.jdbc.DBNResource;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.dbn.common.util.Unsafe.cast;
import static com.dbn.common.util.Unsafe.silent;

class XmlTypeDelegate {

    private static final Map<ClassLoader, XmlTypeDelegate> instances = new ConcurrentHashMap<>();

    private final Class<?> xmlTypeClass;
    private final Class<?> opaqueClass;
    private final Class<?> datumClass;
    private final Class<?> statementClass;
    private final Class<?> resultSetClass;

    private final Method statementOpaqueMethod;
    private final Method resultSetOpaqueMethod;
    private final Method createXmlFromOpaqueMethod;
    private final Method createXmlFromStringMethod;
    private final Method updateResultSetObjectMethod;
    private final Method stringValueMethod;

    @SneakyThrows
    private XmlTypeDelegate(ClassLoader classLoader) {
        xmlTypeClass = classLoader.loadClass("oracle.xdb.XMLType");
        opaqueClass = classLoader.loadClass("oracle.sql.OPAQUE");
        datumClass = classLoader.loadClass("oracle.sql.Datum");
        statementClass = classLoader.loadClass("oracle.jdbc.OracleCallableStatement");
        resultSetClass = classLoader.loadClass("oracle.jdbc.OracleResultSet");

        statementOpaqueMethod = statementClass.getMethod("getOPAQUE", int.class);
        resultSetOpaqueMethod = resultSetClass.getMethod("getOPAQUE", int.class);
        createXmlFromOpaqueMethod = xmlTypeClass.getMethod("createXML", opaqueClass);
        createXmlFromStringMethod = xmlTypeClass.getMethod("createXML", Connection.class, String.class);
        updateResultSetObjectMethod = resultSetClass.getMethod("updateOracleObject", int.class, datumClass);
        stringValueMethod = xmlTypeClass.getMethod("getStringVal");
    }

    static XmlTypeDelegate get(Object object) {
        if (object instanceof DBNResource) {
            object = ((DBNResource<?>) object).getInner();
        }

        ClassLoader classLoader = object.getClass().getClassLoader();
        return instances.computeIfAbsent(classLoader, cl -> new XmlTypeDelegate(cl));
    }

    Object getOpaque(CallableStatement callableStatement, int parameterIndex) {
        return invoke(statementOpaqueMethod, callableStatement, parameterIndex);
    }

    Object getOpaque(ResultSet resultSet, int columnIndex) {
        return invoke(resultSetOpaqueMethod, resultSet, columnIndex);
    }

    Object createXml(Object opaque) {
        return invoke(createXmlFromOpaqueMethod, null, opaque);
    }

    Object createXml(Connection connection, String value) {
        return invoke(createXmlFromStringMethod, null, connection, value);
    }

    String getStringValue(Object xmlType) {
        return invoke(stringValueMethod, xmlType);
    }

    void updateResultSetObject(ResultSet resultSet, int columnIndex, Object xmlType) {
        invoke(updateResultSetObjectMethod, resultSet, columnIndex, xmlType);
    }

    boolean isXmlType(Object opaque) {
        return xmlTypeClass.isAssignableFrom(opaque.getClass());
    }

    private static <T> T invoke(Method method, Object obj, Object ... args) {
        return cast(silent(null, () -> method.invoke(obj, args)));
    }
}
