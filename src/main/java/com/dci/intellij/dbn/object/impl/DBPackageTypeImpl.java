package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.browser.DatabaseBrowserUtils;
import com.dci.intellij.dbn.browser.model.BrowserTreeNode;
import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.common.metadata.def.DBTypeMetadata;
import com.dci.intellij.dbn.object.DBPackage;
import com.dci.intellij.dbn.object.DBPackageType;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.list.DBObjectListContainer;
import com.dci.intellij.dbn.object.filter.type.ObjectTypeFilterSettings;
import com.dci.intellij.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

import static com.dci.intellij.dbn.object.common.property.DBObjectProperty.NAVIGABLE;
import static com.dci.intellij.dbn.object.type.DBObjectType.PACKAGE_TYPE;
import static com.dci.intellij.dbn.object.type.DBObjectType.TYPE_ATTRIBUTE;

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
        properties.set(NAVIGABLE, true);
    }

    @Override
    protected void initLists(ConnectionHandler connection) {
        DBObjectListContainer childObjects = ensureChildObjects();
        childObjects.createObjectList(TYPE_ATTRIBUTE, this);
    }

    @Override
    public DBPackage getPackage() {
        return getParentObject();
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return PACKAGE_TYPE;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return isCollection() ? Icons.DBO_TYPE_COLLECTION : Icons.DBO_TYPE;
    }

    @Override
    @NotNull
    public List<BrowserTreeNode> buildPossibleTreeChildren() {
        return DatabaseBrowserUtils.createList(getChildObjectList(TYPE_ATTRIBUTE));
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
