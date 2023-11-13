package com.dbn.data.value;

import com.dbn.connection.jdbc.DBNConnection;
import com.dbn.connection.jdbc.DBNResultSet;
import com.dbn.data.type.GenericDataType;
import org.jetbrains.annotations.Nullable;

import java.sql.*;

public class XmlTypeValue extends LargeObjectValue{
    //private XMLType xmlType;
    private Object xmlType;

    public XmlTypeValue() {
    }

    public XmlTypeValue(CallableStatement callableStatement, int parameterIndex) throws SQLException {
/*
        OracleCallableStatement oracleCallableStatement = (OracleCallableStatement) callableStatement;
        OPAQUE opaque = oracleCallableStatement.getOPAQUE(parameterIndex);
        if (opaque instanceof XMLType) {
            xmlType = (XMLType) opaque;
        } else {
            xmlType = opaque == null ? null : XMLType.createXML(opaque);
        }
*/

        XmlTypeDelegate d = XmlTypeDelegate.get(callableStatement);
        Object opaque = d.getOpaque(callableStatement, parameterIndex);
        if (opaque == null) return;

        xmlType = d.isXmlType(opaque) ? opaque : d.createXml(opaque);
    }

    public XmlTypeValue(ResultSet resultSet, int columnIndex) throws SQLException {
/*
        resultSet = DBNResultSet.getInner(resultSet);

        OracleResultSet oracleResultSet = (OracleResultSet) resultSet;
        OPAQUE opaque = oracleResultSet.getOPAQUE(columnIndex);
        if (opaque instanceof XMLType) {
            xmlType = (XMLType) opaque;
        } else {
            xmlType = opaque == null ? null : XMLType.createXML(opaque);
        }
*/

        XmlTypeDelegate d = XmlTypeDelegate.get(resultSet);
        resultSet = DBNResultSet.getInner(resultSet);

        Object opaque = d.getOpaque(resultSet, columnIndex);
        if (opaque == null) return;

        xmlType = d.isXmlType(opaque) ? opaque : d.createXml(opaque);
    }



    @Override
    public GenericDataType getGenericDataType() {
        return GenericDataType.XMLTYPE;
    }

    @Override
    @Nullable
    public String read() throws SQLException {
        return read(0);
    }

    @Nullable
    @Override
    public String export() throws SQLException {
        return read();
    }

    @Override
    @Nullable
    public String read(int maxSize) throws SQLException {
        if (xmlType == null) return null;
        XmlTypeDelegate d = XmlTypeDelegate.get(xmlType);
        return xmlType == null ? null : d.getStringValue(xmlType);
    }


    @Override
    public void write(Connection connection, PreparedStatement preparedStatement, int parameterIndex, @Nullable String value) throws SQLException {
/*
        connection = DBNConnection.getInner(connection);
        xmlType = XMLType.createXML(connection, value);
        preparedStatement.setObject(parameterIndex, xmlType);
*/

        XmlTypeDelegate d = XmlTypeDelegate.get(preparedStatement);
        connection = DBNConnection.getInner(connection);
        xmlType = d.createXml(connection, value);
        preparedStatement.setObject(parameterIndex, xmlType);
    }

    @Override
    public void write(Connection connection, ResultSet resultSet, int columnIndex, @Nullable String value) throws SQLException {
/*
        connection = DBNConnection.getInner(connection);
        resultSet = DBNResultSet.getInner(resultSet);

        OracleResultSet oracleResultSet = (OracleResultSet) resultSet;
        xmlType = value == null ? null : XMLType.createXML(connection, value);
        oracleResultSet.updateOracleObject(columnIndex, xmlType);
*/

        XmlTypeDelegate d = XmlTypeDelegate.get(resultSet);
        connection = DBNConnection.getInner(connection);
        resultSet = DBNResultSet.getInner(resultSet);

        xmlType = value == null ? null : d.createXml(connection, value);
        d.updateResultSetObject(resultSet, columnIndex, xmlType);
    }

    @Override
    public void release() {

    }

    @Override
    public long size() throws SQLException {
        return 0;
    }

    @Override
    public String getDisplayValue() {
        return "[XMLTYPE]";
    }
}
