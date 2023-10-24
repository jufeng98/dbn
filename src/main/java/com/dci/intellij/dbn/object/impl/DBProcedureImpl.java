package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.browser.ui.HtmlToolTipBuilder;
import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.common.metadata.def.DBProcedureMetadata;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.object.DBProcedure;
import com.dci.intellij.dbn.object.DBProgram;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.object.common.status.DBObjectStatus;
import com.dci.intellij.dbn.object.common.status.DBObjectStatusHolder;
import com.dci.intellij.dbn.object.type.DBMethodType;
import com.dci.intellij.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.sql.SQLException;

class DBProcedureImpl extends DBMethodImpl<DBProcedureMetadata> implements DBProcedure {
    DBProcedureImpl(DBSchemaObject parent, DBProcedureMetadata metadata) throws SQLException {
        // type functions are not editable independently
        super(parent, metadata);
        assert this.getClass() != DBProcedureImpl.class;
    }

    DBProcedureImpl(DBSchema schema, DBProcedureMetadata metadata) throws SQLException {
        super(schema, metadata);
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBProcedureMetadata metadata) throws SQLException {
        super.initObject(connection, parentObject, metadata);
        return metadata.getProcedureName();
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return DBObjectType.PROCEDURE;
    }

    @Override
    @Nullable
    public Icon getIcon() {
        if (getContentType() == DBContentType.CODE) {
            DBObjectStatusHolder objectStatus = getStatus();
            if (objectStatus.is(DBObjectStatus.VALID)) {
                if (objectStatus.is(DBObjectStatus.DEBUG)){
                    return Icons.DBO_PROCEDURE_DEBUG;
                }
            } else {
                return Icons.DBO_PROCEDURE_ERR;
            }

        }
        return Icons.DBO_PROCEDURE;
    }

    @Override
    public Icon getOriginalIcon() {
        return Icons.DBO_PROCEDURE;
    }

    @Override
    public void buildToolTip(HtmlToolTipBuilder ttb) {
        ttb.append(true, getObjectType().getName(), true);
        ttb.createEmptyRow();
        super.buildToolTip(ttb);
    }


    @Override
    public DBProgram getProgram() {
        return null;
    }

    @Override
    public DBMethodType getMethodType() {
        return DBMethodType.PROCEDURE;
    }

    @Override
    public String getCodeParseRootId(DBContentType contentType) {
        return getParentObject() instanceof DBSchema && contentType == DBContentType.CODE ? "procedure_declaration" : null;
    }
}