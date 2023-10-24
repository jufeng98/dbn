package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.common.metadata.def.DBCharsetMetadata;
import com.dci.intellij.dbn.object.DBCharset;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBRootObjectImpl;
import com.dci.intellij.dbn.object.type.DBObjectType;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

@Getter
class DBCharsetImpl extends DBRootObjectImpl<DBCharsetMetadata> implements DBCharset {
    private String displayName;
    private boolean deprecated;
    private int maxLength;

    public DBCharsetImpl(ConnectionHandler connection, DBCharsetMetadata resultSet) throws SQLException {
        super(connection, resultSet);
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBCharsetMetadata metadata) throws SQLException {
        displayName = metadata.getDisplayName();
        deprecated = metadata.isDeprecated();
        maxLength = metadata.getMaxLength();
        return metadata.getCharsetName();
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return DBObjectType.CHARSET;
    }
}
