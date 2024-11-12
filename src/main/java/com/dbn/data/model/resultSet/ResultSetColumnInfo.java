package com.dbn.data.model.resultSet;

import com.dbn.connection.ConnectionHandler;
import com.dbn.data.model.basic.BasicColumnInfo;
import com.dbn.data.type.DBDataType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ResultSetColumnInfo extends BasicColumnInfo {
    private final int resultSetIndex;

    public ResultSetColumnInfo(ConnectionHandler connection, ResultSet resultSet, int index) throws SQLException {
        super(null, null, index);
        resultSetIndex = index + 1;
        ResultSetMetaData metaData = resultSet.getMetaData();
        name = translateName(metaData.getColumnLabel(resultSetIndex).intern(), connection);

        String dataTypeName = metaData.getColumnTypeName(resultSetIndex);
        int precision = getPrecision(metaData);
        int scale = metaData.getScale(resultSetIndex);

        dataType = DBDataType.get(connection, dataTypeName, precision, precision, scale, false);
    }

    public ResultSetColumnInfo(String name, DBDataType dataType, int columnIndex, int resultSetIndex) {
        super(name, dataType, columnIndex);
        this.resultSetIndex = resultSetIndex;
    }


    // lenient approach for oracle bug returning the size of LOBs instead of the precision.
    private int getPrecision(ResultSetMetaData metaData) throws SQLException {
        try {
            return metaData.getPrecision(resultSetIndex);
        } catch (NumberFormatException e) {
            conditionallyLog(e);
            return 4000;
        }
    }

    public String translateName(String name, ConnectionHandler connection) {
        return name;
    }
}
