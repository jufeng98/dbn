package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.browser.model.BrowserTreeNode;
import com.dci.intellij.dbn.browser.ui.HtmlToolTipBuilder;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.common.metadata.def.DBNestedTableMetadata;
import com.dci.intellij.dbn.object.*;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBObjectImpl;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.dci.intellij.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class DBNestedTableImpl extends DBObjectImpl<DBNestedTableMetadata> implements DBNestedTable {
    private List<DBNestedTableColumn> columns;
    private DBObjectRef<DBType> typeRef;

    DBNestedTableImpl(DBTable parent, DBNestedTableMetadata metadata) throws SQLException {
        super(parent, metadata);

    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBNestedTableMetadata metadata) throws SQLException {
        String name = metadata.getNestedTableName();

        String typeOwner = metadata.getDeclaredTypeOwner();
        String typeName = metadata.getDeclaredTypeName();
        DBSchema schema = connection.getObjectBundle().getSchema(typeOwner);
        typeRef = DBObjectRef.of(schema == null ? null : schema.getType(typeName));
        // todo !!!
        return name;
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return DBObjectType.NESTED_TABLE;
    }

    @Override
    public List<DBNestedTableColumn> getColumns() {
        if (columns == null) {
            columns = new ArrayList<>();
            //todo
        }
        return columns;
    }

    @Override
    public DBNestedTableColumn getColumn(String name) {
        return getChildObject(DBObjectType.COLUMN, name);
    }

    @Override
    public DBTable getTable() {
        return getParentObject();
    }

    public DBType getType() {
        return DBObjectRef.get(typeRef);
    }

    @Override
    public void buildToolTip(HtmlToolTipBuilder ttb) {
        ttb.append(true, getObjectType().getName(), true);
        ttb.createEmptyRow();
        super.buildToolTip(ttb);
    }

    /*********************************************************
     *                     TreeElement                       *
     *********************************************************/

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    @NotNull
    public List<BrowserTreeNode> buildPossibleTreeChildren() {
        return EMPTY_TREE_NODE_LIST;
        //return getColumns();
    }

    @Override
    public boolean hasVisibleTreeChildren() {
        return false;
        //ObjectTypeFilterSettings settings = getConnection().getSettings().getFilterSettings().getObjectTypeFilterSettings();
        //return settings.isVisible(DBObjectType.COLUMN);
    }
}
