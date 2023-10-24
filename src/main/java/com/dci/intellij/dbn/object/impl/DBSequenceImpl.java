package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.browser.ui.HtmlToolTipBuilder;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.common.metadata.def.DBSequenceMetadata;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.DBSequence;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBSchemaObjectImpl;
import com.dci.intellij.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

import static com.dci.intellij.dbn.object.common.property.DBObjectProperty.REFERENCEABLE;
import static com.dci.intellij.dbn.object.common.property.DBObjectProperty.SCHEMA_OBJECT;

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
        properties.set(REFERENCEABLE, true);
        properties.set(SCHEMA_OBJECT, true);
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
