package com.dbn.object.impl;

import com.dbn.browser.ui.HtmlToolTipBuilder;
import com.dbn.connection.ConnectionHandler;
import com.dbn.database.common.metadata.def.DBIndexMetadata;
import com.dbn.object.DBColumn;
import com.dbn.object.DBDataset;
import com.dbn.object.DBIndex;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.DBSchemaObjectImpl;
import com.dbn.object.common.list.DBObjectListContainer;
import com.dbn.object.common.list.DBObjectNavigationList;
import com.dbn.object.common.property.DBObjectProperty;
import com.dbn.object.common.status.DBObjectStatus;
import com.dbn.object.properties.PresentableProperty;
import com.dbn.object.properties.SimplePresentableProperty;
import com.dbn.object.type.DBObjectRelationType;
import com.dbn.object.type.DBObjectType;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class DBIndexImpl extends DBSchemaObjectImpl<DBIndexMetadata> implements DBIndex {
    DBIndexImpl(DBDataset dataset, DBIndexMetadata metadata) throws SQLException {
        super(dataset, metadata);
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBIndexMetadata metadata) throws SQLException {
        String name = metadata.getIndexName();
        set(DBObjectProperty.UNIQUE, metadata.isUnique());
        return name;
    }

    @Override
    public void initStatus(DBIndexMetadata metadata) throws SQLException {
        boolean valid = metadata.isValid();
        getStatus().set(DBObjectStatus.VALID, valid);
    }

    @Override
    public void initProperties() {
        properties.set(DBObjectProperty.SCHEMA_OBJECT, true);
        properties.set(DBObjectProperty.INVALIDABLE, true);
    }

    @Override
    protected void initLists(ConnectionHandler connection) {
        super.initLists(connection);
        DBDataset dataset = getDataset();
        if (dataset != null) {
            DBObjectListContainer childObjects = ensureChildObjects();
            childObjects.createSubcontentObjectList(DBObjectType.COLUMN, this, dataset, DBObjectRelationType.INDEX_COLUMN);
        }
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return DBObjectType.INDEX;
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
        return getChildObjects(DBObjectType.COLUMN);
    }

    private String getColumnsDesc() {
        return getColumns().stream()
                .map(DBObject::getName)
                .collect(Collectors.joining(", "));
    }

    @Override
    public boolean isUnique() {
        return is(DBObjectProperty.UNIQUE);
    }

    @Override
    protected @Nullable List<DBObjectNavigationList> createNavigationLists() {
        List<DBObjectNavigationList> navigationLists = new LinkedList<>();

        List<DBColumn> columns = getColumns();
        if (!columns.isEmpty()) {
            navigationLists.add(DBObjectNavigationList.create("Columns", columns));
        }
        navigationLists.add(DBObjectNavigationList.create("Dataset", getDataset()));

        return navigationLists;
    }

    @Override
    public String getPresentableText() {
        String presentableText = super.getPresentableText();
        String columnsDesc = getColumnsDesc();
        if (StringUtils.isNotBlank(columnsDesc)) {
            return presentableText + "(" + columnsDesc + ") ";
        }
        return presentableText;
    }

    @Override
    public List<PresentableProperty> getPresentableProperties() {
        List<PresentableProperty> properties = super.getPresentableProperties();
        String columnsDesc = getColumnsDesc();
        if (StringUtils.isNotBlank(columnsDesc)) {
            properties.add(0, new SimplePresentableProperty("Columns", columnsDesc));
        }
        return properties;
    }

    @Override
    public void buildToolTip(HtmlToolTipBuilder ttb) {
        ttb.append(true, getObjectType().getName(), true);
        ttb.createEmptyRow();
        String columnsDesc = getColumnsDesc();
        if (StringUtils.isNotBlank(columnsDesc)) {
            ttb.append(true, "columns: " + columnsDesc, false);
        }
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
