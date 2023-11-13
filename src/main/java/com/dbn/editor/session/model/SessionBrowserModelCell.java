package com.dbn.editor.session.model;


import com.dbn.connection.ConnectionHandler;
import com.dbn.data.model.resultSet.ResultSetColumnInfo;
import com.dbn.data.model.resultSet.ResultSetDataModelCell;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionBrowserModelCell
        extends ResultSetDataModelCell<SessionBrowserModelRow, SessionBrowserModel>
        implements ChangeListener {

    public SessionBrowserModelCell(SessionBrowserModelRow row, ResultSet resultSet, ResultSetColumnInfo columnInfo) throws SQLException {
        super(row, resultSet, columnInfo);
    }

    @NotNull
    @Override
    public SessionBrowserModel getModel() {
        return super.getModel();
    }

    @NotNull
    @Override
    public SessionBrowserModelRow getRow() {
        return super.getRow();
    }

    @Override
    public ResultSetColumnInfo getColumnInfo() {
        return (ResultSetColumnInfo) super.getColumnInfo();
    }

    @NotNull
    public ConnectionHandler getConnection() {
        return getRow().getModel().getConnection();
    }

    /*********************************************************
     *                    ChangeListener                     *
     *********************************************************/
    @Override
    public void stateChanged(ChangeEvent e) {
    }
}
