package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.common.metadata.def.DBPrivilegeMetadata;
import com.dci.intellij.dbn.object.DBPrivilege;
import com.dci.intellij.dbn.object.DBRole;
import com.dci.intellij.dbn.object.DBUser;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBRootObjectImpl;
import com.dci.intellij.dbn.object.common.list.DBObjectListContainer;
import com.dci.intellij.dbn.object.common.list.DBObjectNavigationList;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static com.dci.intellij.dbn.object.type.DBObjectType.ROLE;
import static com.dci.intellij.dbn.object.type.DBObjectType.USER;

abstract class DBPrivilegeImpl<M extends DBPrivilegeMetadata> extends DBRootObjectImpl<M> implements DBPrivilege {

    DBPrivilegeImpl(ConnectionHandler connection, M metadata) throws SQLException {
        super(connection, metadata);
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, M metadata) throws SQLException {
        return metadata.getPrivilegeName();
    }

    @Override
    protected void initLists(ConnectionHandler connection) {
        DBObjectListContainer childObjects = ensureChildObjects();
        childObjects.createSubcontentObjectList(USER, this, getObjectBundle(), USER);
        childObjects.createSubcontentObjectList(ROLE, this, getObjectBundle(), ROLE);
    }

    @Override
    public List<DBUser> getUserGrantees() {
        return getChildObjects(USER);
    }

    public List<DBRole> getRoleGrantees() {
        return getChildObjects(ROLE);
    }

    @Override
    protected @Nullable List<DBObjectNavigationList> createNavigationLists() {
        List<DBObjectNavigationList> navigationLists = new LinkedList<>();
        navigationLists.add(DBObjectNavigationList.create("User grantees", getUserGrantees()));

        if (ROLE.isSupported(this)) {
            navigationLists.add(DBObjectNavigationList.create("Role grantees", getRoleGrantees()));
        }
        return navigationLists;
    }

    /*********************************************************
     *                     TreeElement                       *
     *********************************************************/
    @Override
    public boolean isLeaf() {
        return true;
    }
}
