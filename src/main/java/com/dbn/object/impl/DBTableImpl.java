package com.dbn.object.impl;

import com.dbn.browser.DatabaseBrowserUtils;
import com.dbn.browser.model.BrowserTreeNode;
import com.dbn.common.icon.Icons;
import com.dbn.connection.ConnectionHandler;
import com.dbn.database.common.metadata.def.DBTableMetadata;
import com.dbn.editor.DBContentType;
import com.dbn.object.*;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.list.DBObjectListContainer;
import com.dbn.object.common.list.DBObjectNavigationList;
import com.dbn.object.filter.type.ObjectTypeFilterSettings;
import com.dbn.object.properties.PresentableProperty;
import com.dbn.object.properties.SimplePresentableProperty;
import com.dbn.object.type.DBObjectType;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.dbn.object.common.property.DBObjectProperty.TEMPORARY;
import static com.dbn.object.type.DBObjectRelationType.INDEX_COLUMN;
import static com.dbn.object.type.DBObjectType.*;

class DBTableImpl extends DBDatasetImpl<DBTableMetadata> implements DBTable {
    private static final List<DBColumn> EMPTY_COLUMN_LIST = Collections.unmodifiableList(new ArrayList<>());
    private String tableComment;

    DBTableImpl(DBSchema schema, DBTableMetadata metadata) throws SQLException {
        super(schema, metadata);
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBTableMetadata metadata) throws SQLException {
        String name = metadata.getTableName();
        set(TEMPORARY, metadata.isTemporary());
        tableComment = StringUtils.defaultString(metadata.getTableComment());
        return name;
    }

    @Override
    protected void initLists(ConnectionHandler connection) {
        super.initLists(connection);
        DBSchema schema = getSchema();
        DBObjectListContainer childObjects = ensureChildObjects();

        childObjects.createSubcontentObjectList(INDEX, this, schema);
        childObjects.createSubcontentObjectList(NESTED_TABLE, this, schema);
        childObjects.createSubcontentObjectRelationList(INDEX_COLUMN, this, schema);
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return TABLE;
    }

    @Override
    @Nullable
    public Icon getIcon() {
        return isTemporary() ?
                Icons.DBO_TMP_TABLE :
                Icons.DBO_TABLE;
    }

    @Override
    public boolean isTemporary() {
        return is(TEMPORARY);
    }

    @Override
    public @NotNull String getComment() {
        return tableComment;
    }

    @Override
    @Nullable
    public List<DBIndex> getIndexes() {
        return getChildObjects(INDEX);
    }

    @Override
    public List<DBNestedTable> getNestedTables() {
        return getChildObjects(NESTED_TABLE);
    }

    @Override
    @Nullable
    public DBIndex getIndex(String name) {
        return getChildObject(INDEX, name);
    }

    @Override
    public DBNestedTable getNestedTable(String name) {
        return getChildObject(NESTED_TABLE, name);
    }

    @Override
    public List<DBColumn> getPrimaryKeyColumns() {
        List<DBColumn> columns = null;
        for (DBColumn column : getColumns()) {
            if (column.isPrimaryKey()) {
                if (columns == null) {
                    columns = new ArrayList<>();
                }
                columns.add(column);
            }
        }
        return columns == null ? EMPTY_COLUMN_LIST : columns ;
    }

    @Override
    public List<DBColumn> getForeignKeyColumns() {
        List<DBColumn> columns = null;
        for (DBColumn column : getColumns()) {
            if (column.isForeignKey()) {
                if (columns == null) {
                    columns = new ArrayList<>();
                }
                columns.add(column);
            }
        }
        return columns == null ? EMPTY_COLUMN_LIST : columns ;
    }

    @Override
    public List<DBColumn> getUniqueKeyColumns() {
        List<DBColumn> columns = null;
        for (DBColumn column : getColumns()) {
            if (column.isUniqueKey()) {
                if (columns == null) {
                    columns = new ArrayList<>();
                }
                columns.add(column);
            }
        }
        return columns == null ? EMPTY_COLUMN_LIST : columns ;
    }

    @Override
    public boolean isEditable(DBContentType contentType) {
        return contentType == DBContentType.DATA;
    }


    @Override
    protected @Nullable List<DBObjectNavigationList> createNavigationLists() {
        List<DBObjectNavigationList> navigationLists = new LinkedList<>();
        List<DBIndex> indexes = getChildObjects(INDEX);
        if (indexes != null && indexes.size() > 0) {
            navigationLists.add(DBObjectNavigationList.create("Indexes", indexes));
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
                getChildObjectList(COLUMN),
                getChildObjectList(CONSTRAINT),
                getChildObjectList(INDEX),
                getChildObjectList(DATASET_TRIGGER),
                getChildObjectList(NESTED_TABLE));
    }

    @Override
    public boolean hasVisibleTreeChildren() {
        ObjectTypeFilterSettings settings = getObjectTypeFilterSettings();
        return
            settings.isVisible(COLUMN) ||
            settings.isVisible(CONSTRAINT) ||
            settings.isVisible(INDEX) ||
            settings.isVisible(DATASET_TRIGGER) ||
            settings.isVisible(NESTED_TABLE);
    }

    @Override
    public List<PresentableProperty> getPresentableProperties() {
        List<PresentableProperty> properties = super.getPresentableProperties();

        properties.add(0, new SimplePresentableProperty("Comment", getComment()));

        if (isTemporary()) {
            properties.add(0, new SimplePresentableProperty("Attributes", "temporary"));
        }
        return properties;
    }

    @Override
    public String getPresentableText() {
        String comment = getComment();
        String s = StringUtils.isNotBlank(comment) ? "(" + comment + ")" : "";
        return super.getPresentableText() + s;
    }

}
