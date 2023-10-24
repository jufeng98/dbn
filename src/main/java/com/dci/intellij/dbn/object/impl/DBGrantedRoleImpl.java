package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.common.metadata.def.DBGrantedRoleMetadata;
import com.dci.intellij.dbn.object.DBGrantedRole;
import com.dci.intellij.dbn.object.DBPrivilege;
import com.dci.intellij.dbn.object.DBRole;
import com.dci.intellij.dbn.object.DBRoleGrantee;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBObjectImpl;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.dci.intellij.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;

import static com.dci.intellij.dbn.object.common.property.DBObjectProperty.ADMIN_OPTION;
import static com.dci.intellij.dbn.object.common.property.DBObjectProperty.DEFAULT_ROLE;

class DBGrantedRoleImpl extends DBObjectImpl<DBGrantedRoleMetadata> implements DBGrantedRole {
    private DBObjectRef<DBRole> role;

    public DBGrantedRoleImpl(DBRoleGrantee grantee, DBGrantedRoleMetadata metadata) throws SQLException {
        super(grantee, metadata);
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBGrantedRoleMetadata metadata) throws SQLException {
        String name = metadata.getGrantedRoleName();
        this.role = DBObjectRef.of(connection.getObjectBundle().getRole(name));
        set(ADMIN_OPTION, metadata.isAdminOption());
        set(DEFAULT_ROLE, metadata.isDefaultRole());
        return name;
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return DBObjectType.GRANTED_ROLE;
    }

    @Override
    public DBRoleGrantee getGrantee() {
        return (DBRoleGrantee) getParentObject();
    }

    @Override
    public DBRole getRole() {
        return DBObjectRef.get(role);
    }

    @Override
    public boolean isAdminOption() {
        return is(ADMIN_OPTION);
    }

    @Override
    public boolean isDefaultRole() {
        return is(DEFAULT_ROLE);
    }

    @Override
    public boolean hasPrivilege(DBPrivilege privilege) {
        DBRole role = getRole();
        return role != null && role.hasPrivilege(privilege);
    }

    @Nullable
    @Override
    public DBObject getDefaultNavigationObject() {
        return getRole();
    }

    /*********************************************************
     *                     TreeElement                       *
     *********************************************************/
    @Override
    public boolean isLeaf() {
        return true;
    }


}
