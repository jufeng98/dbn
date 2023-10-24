package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.browser.ui.HtmlToolTipBuilder;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.common.metadata.def.DBIndexMetadata;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;
import com.dci.intellij.dbn.object.DBIndex;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBSchemaObjectImpl;
import com.dci.intellij.dbn.object.common.list.DBObjectListContainer;
import com.dci.intellij.dbn.object.common.list.DBObjectNavigationList;
import com.dci.intellij.dbn.object.common.status.DBObjectStatus;
import com.dci.intellij.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static com.dci.intellij.dbn.object.common.property.DBObjectProperty.*;
import static com.dci.intellij.dbn.object.type.DBObjectRelationType.INDEX_COLUMN;
import static com.dci.intellij.dbn.object.type.DBObjectType.COLUMN;
import static com.dci.intellij.dbn.object.type.DBObjectType.INDEX;

class DBIndexImpl extends DBSchemaObjectImpl<DBIndexMetadata> implements DBIndex {
    DBIndexImpl(DBDataset dataset, DBIndexMetadata metadata) throws SQLException {
        super(dataset, metadata);
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBIndexMetadata metadata) throws SQLException {
        String name = metadata.getIndexName();
        set(UNIQUE, metadata.isUnique());
        return name;
    }

    @Override
    public void initStatus(DBIndexMetadata metadata) throws SQLException {
        boolean valid = metadata.isValid();
        getStatus().set(DBObjectStatus.VALID, valid);
    }

    @Override
    public void initProperties() {
        properties.set(SCHEMA_OBJECT, true);
        properties.set(INVALIDABLE, true);
    }

    @Override
    protected void initLists(ConnectionHandler connection) {
        super.initLists(connection);
        DBDataset dataset = getDataset();
        if (dataset != null) {
            DBObjectListContainer childObjects = ensureChildObjects();
            childObjects.createSubcontentObjectList(COLUMN, this, dataset, INDEX_COLUMN);
        }
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return INDEX;
    }

    @NotNull
    @Override
    public String getQualifiedName() {
        return getSchemaName() + '.' + getName();
    }

    @Override
    public DBDataset getDataset() {
        return getParentObject();
    }

    @Override
    public List<DBColumn> getColumns() {
        return getChildObjects(COLUMN);
    }

    @Override
    public boolean isUnique() {
        return is(UNIQUE);
    }

    @Override
    protected @Nullable List<DBObjectNavigationList> createNavigationLists() {
        List<DBObjectNavigationList> navigationLists = new LinkedList<>();

        List<DBColumn> columns = getColumns();
        if (columns.size() > 0) {
            navigationLists.add(DBObjectNavigationList.create("Columns", columns));
        }
        navigationLists.add(DBObjectNavigationList.create("Dataset", getDataset()));

        return navigationLists;
    }

    @Override
    public void buildToolTip(HtmlToolTipBuilder ttb) {
        ttb.append(true, getObjectType().getName(), true);
        ttb.createEmptyRow();
        super.buildToolTip(ttb);
    }

    /********************************************************
     *                   TreeeElement                       *
     * ******************************************************/

    @Override
    public boolean isLeaf() {
        return true;
    }
}
