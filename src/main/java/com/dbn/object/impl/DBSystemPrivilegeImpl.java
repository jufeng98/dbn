package com.dbn.object.impl;

import com.dbn.connection.ConnectionHandler;
import com.dbn.database.common.metadata.def.DBPrivilegeMetadata;
import com.dbn.object.DBSystemPrivilege;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.list.DBObjectList;
import com.dbn.object.common.property.DBObjectProperty;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

class DBSystemPrivilegeImpl extends DBPrivilegeImpl<DBPrivilegeMetadata> implements DBSystemPrivilege {



    public DBSystemPrivilegeImpl(ConnectionHandler connection, DBPrivilegeMetadata metadata) throws SQLException {
        super(connection, metadata);
    }

    private Byte getUserListSignature() {
        DBObjectList<DBObject> objectList = getObjectBundle().getObjectLists().getObjectList(DBObjectType.USER);
        return objectList == null ? 0 : objectList.getSignature();
    }

    @Override
    protected void initProperties() {
        properties.set(DBObjectProperty.ROOT_OBJECT, true);
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return DBObjectType.SYSTEM_PRIVILEGE;
    }
}
