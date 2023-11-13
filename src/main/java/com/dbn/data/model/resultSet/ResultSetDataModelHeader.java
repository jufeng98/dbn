package com.dbn.data.model.resultSet;

import com.dbn.connection.ConnectionHandler;
import com.dbn.data.model.basic.BasicDataModelHeader;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public class ResultSetDataModelHeader<T extends ResultSetColumnInfo> extends BasicDataModelHeader<T> {

    public ResultSetDataModelHeader() {
    }

    public ResultSetDataModelHeader(ConnectionHandler connection, ResultSet resultSet) throws SQLException {
        super();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            T columnInfo = createColumnInfo(connection, resultSet, i);
            addColumnInfo(columnInfo);
        }
    }

    public T getResultSetColumnInfo(int resultSetColumnIndex) {
        List<T> columnInfos = getColumnInfos();
        for (T columnInfo : columnInfos) {
            if (columnInfo.getResultSetIndex() == resultSetColumnIndex) {
                return columnInfo;
            }
        }
        throw new IllegalArgumentException("Invalid result set column index " + resultSetColumnIndex + ". Columns count " + columnInfos.size());
    }

    @NotNull
    public T createColumnInfo(ConnectionHandler connection, ResultSet resultSet, int columnIndex) throws SQLException {
        return (T) new ResultSetColumnInfo(connection, resultSet, columnIndex);
    }
}
