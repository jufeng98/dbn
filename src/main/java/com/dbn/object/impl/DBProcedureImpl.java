package com.dbn.object.impl;

import com.dbn.browser.ui.HtmlToolTipBuilder;
import com.dbn.common.icon.Icons;
import com.dbn.connection.ConnectionHandler;
import com.dbn.database.common.metadata.def.DBProcedureMetadata;
import com.dbn.editor.DBContentType;
import com.dbn.object.DBProcedure;
import com.dbn.object.DBProgram;
import com.dbn.object.DBSchema;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.DBSchemaObject;
import com.dbn.object.common.status.DBObjectStatus;
import com.dbn.object.common.status.DBObjectStatusHolder;
import com.dbn.object.type.DBMethodType;
import com.dbn.object.type.DBObjectType;
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