package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.browser.ui.HtmlToolTipBuilder;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.data.type.DBDataType;
import com.dci.intellij.dbn.database.common.metadata.DBObjectMetadata;
import com.dci.intellij.dbn.object.*;
import com.dci.intellij.dbn.object.common.DBSchemaObjectImpl;
import com.dci.intellij.dbn.object.common.list.DBObjectListContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

import static com.dci.intellij.dbn.object.type.DBObjectRelationType.CONSTRAINT_COLUMN;
import static com.dci.intellij.dbn.object.type.DBObjectType.*;

abstract class DBDatasetImpl<M extends DBObjectMetadata> extends DBSchemaObjectImpl<M> implements DBDataset {
    DBDatasetImpl(DBSchema parent, M metadata) throws SQLException {
        super(parent, metadata);
    }

    @Override
    protected void initLists(ConnectionHandler connection) {
        super.initLists(connection);
        DBSchema schema = getSchema();
        DBObjectListContainer childObjects = ensureChildObjects();

        childObjects.createSubcontentObjectList(COLUMN, this, schema);
        childObjects.createSubcontentObjectList(CONSTRAINT, this, schema);
        childObjects.createSubcontentObjectList(DATASET_TRIGGER, this, schema);
        childObjects.createSubcontentObjectRelationList(CONSTRAINT_COLUMN, this, schema);
    }

    @Override
    @NotNull
    public List<DBColumn> getColumns() {
        return getChildObjects(COLUMN);
    }

    @Override
    @Nullable
    public List<DBConstraint> getConstraints() {
        return getChildObjects(CONSTRAINT);
    }

    @Override
    @Nullable
    public List<DBDatasetTrigger> getTriggers() {
        return getChildObjects(DATASET_TRIGGER);
    }

    @Override
    @Nullable
    public DBColumn getColumn(String name) {
        return getChildObject(COLUMN, name);
    }

    @Override
    @Nullable
    public DBConstraint getConstraint(String name) {
        return getChildObject(CONSTRAINT, name);
    }

    @Override
    @Nullable
    public DBDatasetTrigger getTrigger(String name) {
        return getChildObject(DATASET_TRIGGER, name);
    }

    @Nullable
    @Override
    public List<DBIndex> getIndexes() {
        return null;
    }

    @Nullable
    @Override
    public DBIndex getIndex(String name) {
        return null;
    }

    @Override
    public boolean hasLobColumns() {
        for (DBColumn column : getColumns()) {
            DBDataType dataType = column.getDataType();
            if (dataType.isNative() && dataType.getNativeType().isLargeObject()) {
                return true;
            }

        }
        return false;
    }

    @Override
    public void buildToolTip(HtmlToolTipBuilder ttb) {
        ttb.append(true, getObjectType().getName(), true);
        ttb.createEmptyRow();
        super.buildToolTip(ttb);
    }

}
