package com.dbn.object.impl;

import com.dbn.browser.DatabaseBrowserUtils;
import com.dbn.browser.model.BrowserTreeNode;
import com.dbn.connection.ConnectionHandler;
import com.dbn.database.common.metadata.def.DBMaterializedViewMetadata;
import com.dbn.object.DBIndex;
import com.dbn.object.DBMaterializedView;
import com.dbn.object.DBSchema;
import com.dbn.object.common.list.DBObjectListContainer;
import com.dbn.object.filter.type.ObjectTypeFilterSettings;
import com.dbn.object.type.DBObjectRelationType;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

class DBMaterializedViewImpl extends DBViewImpl implements DBMaterializedView {
    DBMaterializedViewImpl(DBSchema schema, DBMaterializedViewMetadata metadata) throws SQLException {
        super(schema, metadata);
    }

    @Override
    protected void initLists(ConnectionHandler connection) {
        super.initLists(connection);
        DBSchema schema = getSchema();
        DBObjectListContainer childObjects = ensureChildObjects();
        childObjects.createSubcontentObjectList(DBObjectType.INDEX, this, schema);
        childObjects.createSubcontentObjectRelationList(DBObjectRelationType.INDEX_COLUMN, this, schema);
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return DBObjectType.MATERIALIZED_VIEW;
    }

    @Override
    @Nullable
    public List<DBIndex> getIndexes() {
        return getChildObjects(DBObjectType.INDEX);
    }

    @Override
    @Nullable
    public DBIndex getIndex(String name) {
        return getChildObject(DBObjectType.INDEX, name);
    }

    /*********************************************************
     *                     TreeElement                       *
     *********************************************************/
    @Override
    @NotNull
    public List<BrowserTreeNode> buildPossibleTreeChildren() {
        return DatabaseBrowserUtils.createList(
                getChildObjectList(DBObjectType.COLUMN),
                getChildObjectList(DBObjectType.CONSTRAINT),
                getChildObjectList(DBObjectType.INDEX),
                getChildObjectList(DBObjectType.DATASET_TRIGGER));
    }

    @Override
    public boolean hasVisibleTreeChildren() {
        ObjectTypeFilterSettings settings = getObjectTypeFilterSettings();
        return
            settings.isVisible(DBObjectType.COLUMN) ||
            settings.isVisible(DBObjectType.CONSTRAINT) ||
            settings.isVisible(DBObjectType.INDEX) ||
            settings.isVisible(DBObjectType.DATASET_TRIGGER);
    }
}
