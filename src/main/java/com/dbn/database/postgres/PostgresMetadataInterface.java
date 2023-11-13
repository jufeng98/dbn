package com.dbn.database.postgres;

import com.dbn.connection.jdbc.DBNConnection;
import com.dbn.database.common.DatabaseMetadataInterfaceImpl;
import com.dbn.database.interfaces.DatabaseInterfaces;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;


public class PostgresMetadataInterface extends DatabaseMetadataInterfaceImpl {

    public PostgresMetadataInterface(DatabaseInterfaces provider) {
        super("postgres_metadata_interface.xml", provider);
    }

    @Override
    public ResultSet loadCompileObjectErrors(String ownerName, String objectName, DBNConnection connection) throws SQLException {
        return null;
    }

    @Override
    public String createDateString(Date date) {
        String dateString = META_DATE_FORMAT.get().format(date);
        return "str_to_date('" + dateString + "', '%Y-%m-%d %T')";
    }

    @Override
    public void terminateSession(Object sessionId, Object serialNumber, boolean immediate, DBNConnection connection) throws SQLException {
        executeStatement(connection, "kill-session", sessionId);
    }

    @Override
    public boolean hasPendingTransactions(@NotNull DBNConnection connection) {
        try {
            Integer state = (Integer) connection.getClass().getMethod("getTransactionState").invoke(connection);
            return state != 0;
        } catch (Exception e) {
            conditionallyLog(e);
            return true;
        }
    }
}