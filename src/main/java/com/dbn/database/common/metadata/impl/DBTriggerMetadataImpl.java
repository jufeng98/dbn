package com.dbn.database.common.metadata.impl;

import com.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dbn.database.common.metadata.def.DBTriggerMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBTriggerMetadataImpl extends DBObjectMetadataBase implements DBTriggerMetadata {

    public DBTriggerMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    @Override
    public String getTriggerName() throws SQLException {
        return getString("TRIGGER_NAME");
    }

    @Override
    public String getDatasetName() throws SQLException {
        return getString("DATASET_NAME");
    }

    @Override
    public String getTriggerType() throws SQLException {
        return getString("TRIGGER_TYPE");
    }

    @Override
    public String getTriggeringEvent() throws SQLException {
        return getString("TRIGGERING_EVENT");
    }

    @Override
    public boolean isForEachRow() throws SQLException {
        return isYesFlag("IS_FOR_EACH_ROW");
    }

    @Override
    public boolean isEnabled() throws SQLException {
        return isYesFlag("IS_ENABLED");
    }

    @Override
    public boolean isValid() throws SQLException {
        return isYesFlag("IS_VALID");
    }

    @Override
    public boolean isDebug() throws SQLException {
        return isYesFlag("IS_DEBUG");
    }

}
