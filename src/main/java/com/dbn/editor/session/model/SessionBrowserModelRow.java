package com.dbn.editor.session.model;

import com.dbn.connection.ConnectionHandler;
import com.dbn.data.model.ColumnInfo;
import com.dbn.data.model.resultSet.ResultSetColumnInfo;
import com.dbn.data.model.resultSet.ResultSetDataModelRow;
import com.dbn.database.interfaces.DatabaseCompatibilityInterface;
import com.dbn.editor.session.SessionIdentifier;
import com.dbn.editor.session.SessionStatus;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionBrowserModelRow
        extends ResultSetDataModelRow<SessionBrowserModel, SessionBrowserModelCell> {

    public SessionBrowserModelRow(SessionBrowserModel model, ResultSet resultSet, int resultSetRowIndex) throws SQLException {
        super(model, resultSet, resultSetRowIndex);
    }

    @NotNull
    @Override
    protected SessionBrowserModelCell createCell(ResultSet resultSet, ColumnInfo columnInfo) throws SQLException {
        return new SessionBrowserModelCell(this, resultSet, (ResultSetColumnInfo) columnInfo);
    }

    @NotNull
    @Override
    public SessionBrowserModel getModel() {
        return super.getModel();
    }

    public String getUser() {
        return (String) getCellValue("USER");
    }

    public String getHost() {
        return (String) getCellValue("HOST");
    }

    public String getStatus() {
        return (String) getCellValue("STATUS");
    }

    public Object getSessionId() {
        return getCellValue("SESSION_ID");
    }

    public Object getSerialNumber() {
        return getCellValue("SERIAL");
    }

    public SessionIdentifier getSessionIdentifier() {
        return new SessionIdentifier(getSessionId(), getSerialNumber());
    }

    public String getSchema() {
        return (String) getCellValue("SCHEMA");
    }

    public SessionStatus getSessionStatus() {
        ConnectionHandler connection = getModel().getConnection();
        DatabaseCompatibilityInterface compatibility = connection.getCompatibilityInterface();
        return compatibility.getSessionStatus(getStatus());
    }

}
