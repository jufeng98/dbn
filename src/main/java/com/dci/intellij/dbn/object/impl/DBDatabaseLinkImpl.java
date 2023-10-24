package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.browser.ui.HtmlToolTipBuilder;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.common.metadata.def.DBDatabaseLinkMetadata;
import com.dci.intellij.dbn.object.DBDatabaseLink;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBSchemaObjectImpl;
import com.dci.intellij.dbn.object.type.DBObjectType;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

import static com.dci.intellij.dbn.object.common.property.DBObjectProperty.SCHEMA_OBJECT;

@Getter
class DBDatabaseLinkImpl extends DBSchemaObjectImpl<DBDatabaseLinkMetadata> implements DBDatabaseLink {
    private String userName;
    private String host;

    DBDatabaseLinkImpl(DBSchema schema, DBDatabaseLinkMetadata metadata) throws SQLException {
        super(schema, metadata);
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBDatabaseLinkMetadata metadata) throws SQLException {
        String name = metadata.getDblinkName();
        userName = metadata.getUserName();
        host = metadata.getHost();
        return name;
    }

    @Override
    public void initProperties() {
        properties.set(SCHEMA_OBJECT, true);
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return DBObjectType.DBLINK;
    }

    @Override
    public void buildToolTip(HtmlToolTipBuilder ttb) {
        ttb.append(true, getObjectType().getName(), true);
        ttb.append(true, host, false);
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
