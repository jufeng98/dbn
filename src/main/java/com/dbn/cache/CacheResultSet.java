package com.dbn.cache;

import com.dbn.connection.jdbc.DBNConnection;
import com.dbn.connection.jdbc.DBNResultSet;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CacheResultSet extends DBNResultSet {
    private final ArrayNode identifierArrayNode;
    private int index;
    private final int count;
    private boolean close;
    private final String identifier;

    public CacheResultSet(ResultSet inner, DBNConnection connection, ArrayNode identifierArrayNode, String identifier) {
        super(inner, connection);
        this.identifierArrayNode = identifierArrayNode;
        this.identifier = identifier;
        index = -1;
        count = identifierArrayNode.size();
        close = false;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean next() throws SQLException {
        index++;
        return index < count;
    }

    @Override
    public void close() throws SQLException {
        close = true;
    }

    @Override
    public boolean isClosedInner() {
        return close;
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        ObjectNode objectNode = (ObjectNode) identifierArrayNode.get(index);
        JsonNode jsonNode = objectNode.get(columnLabel);
        if (jsonNode == null || jsonNode.getClass() == NullNode.class) {
            return null;
        }

        return jsonNode.asText();
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        ObjectNode objectNode = (ObjectNode) identifierArrayNode.get(index);
        JsonNode jsonNode = objectNode.get(columnLabel);
        if (jsonNode == null || jsonNode.getClass() == NullNode.class) {
            return 0;
        }

        return jsonNode.intValue();
    }

    @Override
    public double getDouble(String columnLabel) {
        ObjectNode objectNode = (ObjectNode) identifierArrayNode.get(index);
        JsonNode jsonNode = objectNode.get(columnLabel);
        if (jsonNode == null || jsonNode.getClass() == NullNode.class) {
            return 0;
        }

        return jsonNode.doubleValue();
    }

    @Override
    public float getFloat(String columnLabel) {
        ObjectNode objectNode = (ObjectNode) identifierArrayNode.get(index);
        JsonNode jsonNode = objectNode.get(columnLabel);
        if (jsonNode == null || jsonNode.getClass() == NullNode.class) {
            return 0;
        }

        return jsonNode.floatValue();
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
        ObjectNode objectNode = (ObjectNode) identifierArrayNode.get(index);
        JsonNode jsonNode = objectNode.get(columnLabel);
        if (jsonNode == null || jsonNode.getClass() == NullNode.class) {
            return false;
        }

        return jsonNode.booleanValue();
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) {
        ObjectNode objectNode = (ObjectNode) identifierArrayNode.get(index);
        JsonNode jsonNode = objectNode.get(columnLabel);
        if (jsonNode == null || jsonNode.getClass() == NullNode.class) {
            return null;
        }

        long aLong = jsonNode.longValue();
        return BigDecimal.valueOf(aLong);
    }

    @Override
    public long getLong(String columnLabel) throws SQLException {
        ObjectNode objectNode = (ObjectNode) identifierArrayNode.get(index);
        JsonNode jsonNode = objectNode.get(columnLabel);
        if (jsonNode == null || jsonNode.getClass() == NullNode.class) {
            return 0;
        }

        return jsonNode.longValue();
    }

    @Override
    public short getShort(String columnLabel) throws SQLException {
        ObjectNode objectNode = (ObjectNode) identifierArrayNode.get(index);
        JsonNode jsonNode = objectNode.get(columnLabel);
        if (jsonNode == null || jsonNode.getClass() == NullNode.class) {
            return 0;
        }

        return jsonNode.shortValue();
    }
}
