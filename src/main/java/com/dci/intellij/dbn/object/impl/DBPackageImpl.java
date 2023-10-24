package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.browser.DatabaseBrowserUtils;
import com.dci.intellij.dbn.browser.model.BrowserTreeNode;
import com.dci.intellij.dbn.browser.ui.HtmlToolTipBuilder;
import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.common.metadata.def.DBPackageMetadata;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.object.*;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.list.DBObjectListContainer;
import com.dci.intellij.dbn.object.common.status.DBObjectStatus;
import com.dci.intellij.dbn.object.filter.type.ObjectTypeFilterSettings;
import com.dci.intellij.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

import static com.dci.intellij.dbn.object.type.DBObjectType.*;

class DBPackageImpl
        extends DBProgramImpl<DBPackageMetadata, DBPackageProcedure, DBPackageFunction, DBPackageType>
        implements DBPackage {

    DBPackageImpl(DBSchema schema, DBPackageMetadata metadata) throws SQLException {
        super(schema, metadata);
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBPackageMetadata metadata) throws SQLException {
        return metadata.getPackageName();
    }

    @Override
    protected void initLists(ConnectionHandler connection) {
        super.initLists(connection);
        DBSchema schema = getSchema();
        DBObjectListContainer childObjects = ensureChildObjects();
        childObjects.createSubcontentObjectList(PACKAGE_FUNCTION, this, schema);
        childObjects.createSubcontentObjectList(PACKAGE_PROCEDURE, this, schema);
        childObjects.createSubcontentObjectList(PACKAGE_TYPE, this, schema);
    }

    @Override
    protected DBObjectType getFunctionObjectType() {
        return PACKAGE_FUNCTION;
    }

    @Override
    protected DBObjectType getProcedureObjectType() {
        return PACKAGE_PROCEDURE;
    }

    @Override
    protected DBObjectType getTypeObjectType() {
        return PACKAGE_TYPE;
    }

    @Override
    public List<DBPackageType> getTypes() {
        return getChildObjects(PACKAGE_TYPE);
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return PACKAGE;
    }

    @Override
    @Nullable
    public Icon getIcon() {
        if (getStatus().is(DBObjectStatus.VALID)) {
            if (getStatus().is(DBObjectStatus.DEBUG))  {
                return Icons.DBO_PACKAGE_DEBUG;
            } else {
                return Icons.DBO_PACKAGE;
            }
        } else {
            return Icons.DBO_PACKAGE_ERR;
        }
    }

    @Override
    public Icon getOriginalIcon() {
        return Icons.DBO_PACKAGE;
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
    @NotNull
    public List<BrowserTreeNode> buildPossibleTreeChildren() {
        return DatabaseBrowserUtils.createList(
                getChildObjectList(PACKAGE_PROCEDURE),
                getChildObjectList(PACKAGE_FUNCTION),
                getChildObjectList(PACKAGE_TYPE));
    }

    @Override
    public boolean hasVisibleTreeChildren() {
        ObjectTypeFilterSettings settings = getObjectTypeFilterSettings();
        return
            settings.isVisible(PROCEDURE) ||
            settings.isVisible(FUNCTION) ||
            settings.isVisible(TYPE);
    }

    @Override
    public String getCodeParseRootId(DBContentType contentType) {
        return contentType == DBContentType.CODE_SPEC ? "package_spec" :
               contentType == DBContentType.CODE_BODY ? "package_body" : null;
    }
}
