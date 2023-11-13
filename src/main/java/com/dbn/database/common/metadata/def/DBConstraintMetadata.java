package com.dbn.database.common.metadata.def;

import com.dbn.database.common.metadata.DBObjectMetadata;

import java.sql.SQLException;

public interface DBConstraintMetadata extends DBObjectMetadata {

    String getDatasetName() throws SQLException;

    String getConstraintName() throws SQLException;

    String getConstraintType() throws SQLException;

    String getCheckCondition() throws SQLException;

    String getFkConstraintOwner() throws SQLException;

    String getFkConstraintName() throws SQLException;

    boolean isEnabled() throws SQLException;
}
