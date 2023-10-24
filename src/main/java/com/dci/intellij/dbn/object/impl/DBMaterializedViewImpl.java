package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.browser.DatabaseBrowserUtils;
import com.dci.intellij.dbn.browser.model.BrowserTreeNode;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.common.metadata.def.DBMaterializedViewMetadata;
import com.dci.intellij.dbn.object.DBIndex;
import com.dci.intellij.dbn.object.DBMaterializedView;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.common.list.DBObjectListContainer;
import com.dci.intellij.dbn.object.filter.type.ObjectTypeFilterSettings;
import com.dci.intellij.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

import static com.dci.intellij.dbn.object.type.DBObjectRelationType.INDEX_COLUMN;
import static com.dci.intellij.dbn.object.type.DBObjectType.*;

class DBMaterializedViewImpl extends DBViewImpl implements DBMaterializedView {
    DBMaterializedViewImpl(DBSchema schema, DBMaterializedViewMetadata metadata) throws SQLException {
        super(schema, metadata);
    }

    @Override
    protected void initLists(ConnectionHandler connection) {
        super.initLists(connection);
        DBSchema schema = getSchema();
        DBObjectListContainer childObjects = ensureChildObjects();
        childObjects.createSubcontentObjectList(INDEX, this, schema);
        childObjects.createSubcontentObjectRelationList(INDEX_COLUMN, this, schema);
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return MATERIALIZED_VIEW;
    }

    @Override
    @Nullable
    public List<DBIndex> getIndexes() {
        return getChildObjects(INDEX);
    }

    @Override
    @Nullable
    public DBIndex getIndex(String name) {
        return getChildObject(INDEX, name);
    }

    /*********************************************************
     *                     TreeElement                       *
     *********************************************************/
    @Override
    @NotNull
    public List<BrowserTreeNode> buildPossibleTreeChildren() {
        return DatabaseBrowserUtils.createList(
                getChildObjectList(COLUMN),
                getChildObjectList(CONSTRAINT),
                getChildObjectList(INDEX),
                getChildObjectList(DATASET_TRIGGER));
    }

    @Override
    public boolean hasVisibleTreeChildren() {
        ObjectTypeFilterSettings settings = getObjectTypeFilterSettings();
        return
            settings.isVisible(COLUMN) ||
            settings.isVisible(CONSTRAINT) ||
            settings.isVisible(INDEX) ||
            settings.isVisible(DATASET_TRIGGER);
    }
}
