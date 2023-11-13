package com.dbn.object.impl;

import com.dbn.browser.DatabaseBrowserUtils;
import com.dbn.browser.model.BrowserTreeNode;
import com.dbn.common.icon.Icons;
import com.dbn.connection.ConnectionHandler;
import com.dbn.database.common.metadata.def.DBTypeMetadata;
import com.dbn.object.DBPackage;
import com.dbn.object.DBPackageType;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.list.DBObjectListContainer;
import com.dbn.object.common.property.DBObjectProperty;
import com.dbn.object.filter.type.ObjectTypeFilterSettings;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

class DBPackageTypeImpl extends DBTypeImpl implements DBPackageType {

    DBPackageTypeImpl(DBPackage packagee, DBTypeMetadata metadata) throws SQLException {
        super(packagee, metadata);
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBTypeMetadata metadata) throws SQLException {
        return metadata.getTypeName();
    }

    @Override
    public void initStatus(DBTypeMetadata metadata) {}

    @Override
    public void initProperties() {
        properties.set(DBObjectProperty.NAVIGABLE, true);
    }

    @Override
    protected void initLists(ConnectionHandler connection) {
        DBObjectListContainer childObjects = ensureChildObjects();
        childObjects.createObjectList(DBObjectType.TYPE_ATTRIBUTE, this);
    }

    @Override
    public DBPackage getPackage() {
        return getParentObject();
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return DBObjectType.PACKAGE_TYPE;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return isCollection() ? Icons.DBO_TYPE_COLLECTION : Icons.DBO_TYPE;
    }

    @Override
    @NotNull
    public List<BrowserTreeNode> buildPossibleTreeChildren() {
        return DatabaseBrowserUtils.createList(getChildObjectList(DBObjectType.TYPE_ATTRIBUTE));
    }

    @Override
    public boolean hasVisibleTreeChildren() {
        ObjectTypeFilterSettings settings = getObjectTypeFilterSettings();
        return settings.isVisible(DBObjectType.ATTRIBUTE);
    }

    @Override
    public boolean isEmbedded() {
        return true;
    }
}
