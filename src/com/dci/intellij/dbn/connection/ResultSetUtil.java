package com.dci.intellij.dbn.connection;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.dci.intellij.dbn.common.dispose.DisposableBase;

public class ResultSetUtil extends DisposableBase{
    public static void insertRow(final ResultSet resultSet) throws SQLException {
        try {
            resultSet.insertRow();
        } catch (Throwable e) {
            throw e instanceof SQLException ?
                    (SQLException) e :
                    new SQLException("Error inserting row: [" + e.getClass().getSimpleName() + "] " + e.getMessage());
        }
    }

    public static void moveToInsertRow(final ResultSet resultSet) throws SQLException {
        try {
            resultSet.moveToInsertRow();
        } catch (Throwable e) {
            throw e instanceof SQLException ?
                    (SQLException) e :
                    new SQLException("Error selecting insert row: [" + e.getClass().getSimpleName() + "] " + e.getMessage());
        }
    }
    public static void moveToCurrentRow(final ResultSet resultSet) throws SQLException {
        try {
            resultSet.moveToCurrentRow();
        } catch (Throwable e) {
            throw e instanceof SQLException ?
                    (SQLException) e :
                    new SQLException("Error selecting current row: [" + e.getClass().getSimpleName() + "] " + e.getMessage());
        }
    }

    public static void deleteRow(final ResultSet resultSet) throws SQLException {
        try {
            resultSet.deleteRow();
        } catch (Throwable e) {
            throw e instanceof SQLException ?
                    (SQLException) e :
                    new SQLException("Error deleting row: [" + e.getClass().getSimpleName() + "] " + e.getMessage());
        }
    }


    public static void refreshRow(final ResultSet resultSet) throws SQLException {
        try {
            resultSet.refreshRow();
        } catch (Throwable e) {
            throw e instanceof SQLException ?
                    (SQLException) e :
                    new SQLException("Error refreshing row: [" + e.getClass().getSimpleName() + "] " + e.getMessage());
        }
    }

    public static void updateRow(final ResultSet resultSet) throws SQLException {
        try {
            resultSet.updateRow();
        } catch (Throwable e) {
            throw e instanceof SQLException ?
                    (SQLException) e :
                    new SQLException("Error updating row: [" + e.getClass().getSimpleName() + "] " + e.getMessage());
        }
    }

    public static void absolute(final ResultSet resultSet, int row) throws SQLException {
        try {
            resultSet.absolute(row);
        } catch (Throwable e) {
            throw e instanceof SQLException ?
                    (SQLException) e :
                    new SQLException("Error selecting row: [" + e.getClass().getSimpleName() + "] " + e.getMessage());
        }
    }
}
