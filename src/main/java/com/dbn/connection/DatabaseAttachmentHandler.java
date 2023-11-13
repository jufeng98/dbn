package com.dbn.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseAttachmentHandler {
    void attachDatabase(Connection connection, String filePath, String schemaName) throws SQLException;
}
