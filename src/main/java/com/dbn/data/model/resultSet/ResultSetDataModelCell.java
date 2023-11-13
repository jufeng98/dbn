package com.dbn.data.model.resultSet;

import com.dbn.connection.jdbc.DBNConnection;
import com.dbn.data.model.sortable.SortableDataModelCell;
import com.dbn.data.type.DBDataType;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.dbn.editor.data.model.RecordStatus.INSERTING;

public class ResultSetDataModelCell<
        R extends ResultSetDataModelRow<M, ? extends ResultSetDataModelCell<R, M>>,
        M extends ResultSetDataModel<R, ? extends ResultSetDataModelCell<R, M>>>
        extends SortableDataModelCell<R, M> {

    public ResultSetDataModelCell(R row, ResultSet resultSet, ResultSetColumnInfo columnInfo) throws SQLException {
        super(row, null, columnInfo.getIndex());
        DBDataType dataType = columnInfo.getDataType();
        if (!getModel().is(INSERTING)) {
            Object userValue = dataType.getValueFromResultSet(resultSet, columnInfo.getResultSetIndex());
            setUserValue(userValue);
        }
    }

    @NotNull
    @Override
    public M getModel() {
        return super.getModel();
    }

    @NotNull
    @Override
    public R getRow() {
        return super.getRow();
    }

    @NotNull
    protected DBNConnection getResultConnection() {
        return getRow().getModel().getResultConnection();
    }
}
