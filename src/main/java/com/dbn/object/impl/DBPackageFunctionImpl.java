package com.dbn.object.impl;

import com.dbn.database.common.metadata.def.DBFunctionMetadata;
import com.dbn.editor.DBContentType;
import com.dbn.object.DBPackage;
import com.dbn.object.DBPackageFunction;
import com.dbn.object.DBProgram;
import com.dbn.object.common.property.DBObjectProperty;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

class DBPackageFunctionImpl extends DBFunctionImpl implements DBPackageFunction {


    DBPackageFunctionImpl(DBPackage packagee, DBFunctionMetadata metadata) throws SQLException {
        super(packagee, metadata);
    }

    @Override
    public void initStatus(DBFunctionMetadata metadata) throws SQLException {}

    @Override
    public void initProperties() {
        properties.set(DBObjectProperty.NAVIGABLE, true);
    }

    @Override
    public DBPackage getPackage() {
        return (DBPackage) getParentObject();
    }

    @Override
    public DBProgram getProgram() {
        return getPackage();
    }

    @Override
    public boolean isProgramMethod() {
        return true;
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return DBObjectType.PACKAGE_FUNCTION;
    }

    @Override
    public void executeUpdateDDL(DBContentType contentType, String oldCode, String newCode) throws SQLException {}
}
