package com.dbn.object.impl;

import com.dbn.browser.ui.HtmlToolTipBuilder;
import com.dbn.connection.ConnectionHandler;
import com.dbn.database.common.metadata.def.DBSequenceMetadata;
import com.dbn.object.DBSchema;
import com.dbn.object.DBSequence;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.DBSchemaObjectImpl;
import com.dbn.object.common.property.DBObjectProperty;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

class DBSequenceImpl extends DBSchemaObjectImpl<DBSequenceMetadata> implements DBSequence {
    DBSequenceImpl(DBSchema schema, DBSequenceMetadata resultSet) throws SQLException {
        super(schema, resultSet);
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBSequenceMetadata metadata) throws SQLException {
        return metadata.getSequenceName();
    }

    @Override
    public void initProperties() {
        properties.set(DBObjectProperty.REFERENCEABLE, true);
        properties.set(DBObjectProperty.SCHEMA_OBJECT, true);
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return DBObjectType.SEQUENCE;
    }

    @Override
    public void buildToolTip(HtmlToolTipBuilder ttb) {
        ttb.append(true, getObjectType().getName(), true);
        ttb.createEmptyRow();
        super.buildToolTip(ttb);
    }      

    /*********************************************************
     *                     TreeElement                       *
     *********************************************************/

    @Override
    public boolean isLeaf() {
        return true;
    }
}
