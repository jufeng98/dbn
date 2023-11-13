package com.dbn.object.impl;

import com.dbn.database.common.metadata.def.DBProcedureMetadata;
import com.dbn.editor.DBContentType;
import com.dbn.object.DBProgram;
import com.dbn.object.DBType;
import com.dbn.object.DBTypeProcedure;
import com.dbn.object.common.property.DBObjectProperty;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

class DBTypeProcedureImpl extends DBProcedureImpl implements DBTypeProcedure {
    DBTypeProcedureImpl(DBType type, DBProcedureMetadata metadata) throws SQLException {
        super(type, metadata);
    }

    @Override
    public void initStatus(DBProcedureMetadata metadata) throws SQLException {}

    @Override
    public void initProperties() {
        properties.set(DBObjectProperty.NAVIGABLE, true);
    }

    @Override
    public DBType getType() {
        return getParentObject();
    }

    @Override
    public DBProgram getProgram() {
        return getType();
    }    

    @Override
    public boolean isProgramMethod() {
        return true;
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return DBObjectType.TYPE_PROCEDURE;
    }

    @Override
    public void executeUpdateDDL(DBContentType contentType, String oldCode, String newCode) throws SQLException {}
}