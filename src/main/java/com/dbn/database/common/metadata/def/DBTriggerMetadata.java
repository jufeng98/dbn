package com.dbn.database.common.metadata.def;

import com.dbn.database.common.metadata.DBObjectMetadata;

import java.sql.SQLException;

public interface DBTriggerMetadata extends DBObjectMetadata {

    String getTriggerName() throws SQLException;

    String getDatasetName() throws SQLException;

    String getTriggerType() throws SQLException;

    String getTriggeringEvent() throws SQLException;

    boolean isForEachRow() throws SQLException;

    boolean isEnabled() throws SQLException;

    boolean isValid() throws SQLException;

    boolean isDebug() throws SQLException;
}
