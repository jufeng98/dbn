package com.dbn.object.impl;

import com.dbn.connection.ConnectionHandler;
import com.dbn.database.common.metadata.def.DBGrantedPrivilegeMetadata;
import com.dbn.object.DBGrantedPrivilege;
import com.dbn.object.DBPrivilege;
import com.dbn.object.DBPrivilegeGrantee;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.DBObjectImpl;
import com.dbn.object.common.property.DBObjectProperty;
import com.dbn.object.lookup.DBObjectRef;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;

class DBGrantedPrivilegeImpl extends DBObjectImpl<DBGrantedPrivilegeMetadata> implements DBGrantedPrivilege {
    private DBObjectRef<DBPrivilege> privilege;

    public DBGrantedPrivilegeImpl(DBPrivilegeGrantee grantee, DBGrantedPrivilegeMetadata metadata) throws SQLException {
        super(grantee, metadata);
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBGrantedPrivilegeMetadata metadata) throws SQLException {
        String name = metadata.getGrantedPrivilegeName();
        privilege = DBObjectRef.of(connection.getObjectBundle().getPrivilege(name));
        set(DBObjectProperty.ADMIN_OPTION, metadata.isAdminOption());
        return name;
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return DBObjectType.GRANTED_PRIVILEGE;
    }

    @Override
    public DBPrivilegeGrantee getGrantee() {
        return (DBPrivilegeGrantee) getParentObject();
    }

    @Override
    public DBPrivilege getPrivilege() {
        return DBObjectRef.get(privilege);
    }

    @Override
    public boolean isAdminOption() {
        return is(DBObjectProperty.ADMIN_OPTION);
    }

    @Nullable
    @Override
    public DBObject getDefaultNavigationObject() {
        return getPrivilege();
    }

    /*********************************************************
     *                     TreeElement                       *
     *********************************************************/
    @Override
    public boolean isLeaf() {
        return true;
    }

}
