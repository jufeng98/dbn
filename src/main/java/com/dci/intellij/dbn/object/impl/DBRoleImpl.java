package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.browser.DatabaseBrowserUtils;
import com.dci.intellij.dbn.browser.model.BrowserTreeNode;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.common.metadata.def.DBRoleMetadata;
import com.dci.intellij.dbn.object.*;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBObjectBundle;
import com.dci.intellij.dbn.object.common.DBRootObjectImpl;
import com.dci.intellij.dbn.object.common.list.DBObjectListContainer;
import com.dci.intellij.dbn.object.common.list.DBObjectNavigationList;
import com.dci.intellij.dbn.object.filter.type.ObjectTypeFilterSettings;
import com.dci.intellij.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.dci.intellij.dbn.common.util.Lists.filter;
import static com.dci.intellij.dbn.object.common.property.DBObjectProperty.ROOT_OBJECT;
import static com.dci.intellij.dbn.object.type.DBObjectRelationType.ROLE_PRIVILEGE;
import static com.dci.intellij.dbn.object.type.DBObjectRelationType.ROLE_ROLE;
import static com.dci.intellij.dbn.object.type.DBObjectType.*;

class DBRoleImpl extends DBRootObjectImpl<DBRoleMetadata> implements DBRole {

    public DBRoleImpl(ConnectionHandler connection, DBRoleMetadata metadata) throws SQLException {
        super(connection, metadata);
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBRoleMetadata metadata) throws SQLException {
        return metadata.getRoleName();
    }

    @Override
    protected void initLists(ConnectionHandler connection) {
        DBObjectBundle objectBundle = connection.getObjectBundle();
        DBObjectListContainer childObjects = ensureChildObjects();
        childObjects.createSubcontentObjectList(GRANTED_PRIVILEGE, this, objectBundle, ROLE_PRIVILEGE);
        childObjects.createSubcontentObjectList(GRANTED_ROLE, this, objectBundle, ROLE_ROLE);
    }

    @Override
    protected void initProperties() {
        properties.set(ROOT_OBJECT, true);
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return ROLE;
    }

    @Override
    public List<DBGrantedPrivilege> getPrivileges() {
        return getChildObjects(GRANTED_PRIVILEGE);
    }

    @Override
    public List<DBGrantedRole> getGrantedRoles() {
        return getChildObjects(GRANTED_ROLE);
    }

    @Override
    public List<DBUser> getUserGrantees() {
        List<DBUser> users = getObjectBundle().getUsers();
        if (users == null || users.isEmpty()) return Collections.emptyList();
        return filter(users, u -> u.hasRole(this));
    }

    @Override
    public List<DBRole> getRoleGrantees() {
        List<DBRole> roles = getObjectBundle().getRoles();
        if (roles == null || roles.isEmpty()) return Collections.emptyList();
        return filter(roles, r -> r.hasRole(this));
    }

    @Override
    public boolean hasPrivilege(DBPrivilege privilege) {
        for (DBGrantedPrivilege rolePrivilege : getPrivileges()) {
            if (Objects.equals(rolePrivilege.getPrivilege(), privilege)) {
                return true;
            }
        }
        for (DBGrantedRole inheritedRole : getGrantedRoles()) {
            if (inheritedRole.hasPrivilege(privilege)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasRole(DBRole role) {
        for (DBGrantedRole inheritedRole : getGrantedRoles()) {
            if (Objects.equals(inheritedRole.getRole(), role)) {
                return true;
            }
        }
        return false;
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
    @NotNull
    public List<BrowserTreeNode> buildPossibleTreeChildren() {
        return DatabaseBrowserUtils.createList(
                getChildObjectList(GRANTED_PRIVILEGE),
                getChildObjectList(GRANTED_ROLE));
    }

    @Override
    public boolean hasVisibleTreeChildren() {
        ObjectTypeFilterSettings settings = getObjectTypeFilterSettings();
        return
            settings.isVisible(PRIVILEGE) ||
            settings.isVisible(ROLE);
    }
}
