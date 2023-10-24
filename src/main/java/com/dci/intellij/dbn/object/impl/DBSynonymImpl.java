package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.browser.ui.HtmlToolTipBuilder;
import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.common.metadata.def.DBSynonymMetadata;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.DBSynonym;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBSchemaObjectImpl;
import com.dci.intellij.dbn.object.common.list.DBObjectNavigationList;
import com.dci.intellij.dbn.object.common.status.DBObjectStatus;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.dci.intellij.dbn.object.properties.DBObjectPresentableProperty;
import com.dci.intellij.dbn.object.properties.PresentableProperty;
import com.dci.intellij.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static com.dci.intellij.dbn.object.common.property.DBObjectProperty.*;

class DBSynonymImpl extends DBSchemaObjectImpl<DBSynonymMetadata> implements DBSynonym {
    private DBObjectRef<DBObject> underlyingObject;

    DBSynonymImpl(DBSchema schema, DBSynonymMetadata resultSet) throws SQLException {
        super(schema, resultSet);
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBSynonymMetadata metadata) throws SQLException {
        String name = metadata.getSynonymName();
        String schemaName = metadata.getUnderlyingObjectOwner();
        String objectName = metadata.getUnderlyingObjectName();
        DBObjectType objectType = DBObjectType.get(metadata.getUnderlyingObjectType(), DBObjectType.ANY);

        DBSchema schema = connection.getObjectBundle().getSchema(schemaName);
        if (schema != null) {
            DBObjectRef schemaRef = schema.ref();
            underlyingObject = new DBObjectRef<>(schemaRef, objectType, objectName);
        }

        return name;
    }

    @Override
    public void initStatus(DBSynonymMetadata metadata) throws SQLException {
        boolean valid = metadata.isValid();
        getStatus().set(DBObjectStatus.VALID, valid);
    }

    @Override
    public void initProperties() {
        properties.set(SCHEMA_OBJECT, true);
        properties.set(REFERENCEABLE, true);
        properties.set(INVALIDABLE, true);
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return DBObjectType.SYNONYM;
    }

    @Nullable
    @Override
    public DBObject getDefaultNavigationObject() {
        return getUnderlyingObject();
    }

    @Override
    @Nullable
    public Icon getIcon() {
        if (getStatus().is(DBObjectStatus.VALID)) {
            return Icons.DBO_SYNONYM;
        } else {
            return Icons.DBO_SYNONYM_ERR;
        }
    }

    @Override
    public Icon getOriginalIcon() {
        return Icons.DBO_SYNONYM;
    }

    @Override
    @Nullable
    public DBObject getUnderlyingObject() {
        return DBObjectRef.get(underlyingObject);
    }

    @Override
    public String getNavigationTooltipText() {
        DBObject parentObject = getParentObject();
        if (parentObject == null) {
            return "unknown " + getTypeName();
        } else {
            DBObject underlyingObject = getUnderlyingObject();
            if (underlyingObject == null) {
                return "unknown " + getTypeName() +
                        " (" + parentObject.getTypeName() + " " + parentObject.getName() + ")";
            } else {
                return getTypeName() + " of " + underlyingObject.getName() + " " + underlyingObject.getTypeName() +
                        " (" + parentObject.getTypeName() + " " + parentObject.getName() + ")";

            }

        }
    }

    @Override
    protected @Nullable List<DBObjectNavigationList> createNavigationLists() {
        DBObject underlyingObject = getUnderlyingObject();
        if (underlyingObject != null) {
            List<DBObjectNavigationList> navigationLists = new LinkedList<>();
            navigationLists.add(DBObjectNavigationList.create("Underlying " + underlyingObject.getTypeName(), underlyingObject));
            return navigationLists;
        }
        return null;
    }

    @Override
    public void buildToolTip(HtmlToolTipBuilder ttb) {
        DBObject underlyingObject = getUnderlyingObject();
        if (underlyingObject!= null) {
            ttb.append(true, underlyingObject.getObjectType().getName() + " ", true);
        }
        ttb.append(false, getObjectType().getName(), true);
        ttb.createEmptyRow();
        super.buildToolTip(ttb);
    }

    @Override
    public List<PresentableProperty> getPresentableProperties() {
        List<PresentableProperty> properties = super.getPresentableProperties();
        DBObject underlyingObject = getUnderlyingObject();
        if (underlyingObject != null) {
            properties.add(0, new DBObjectPresentableProperty("Underlying object", underlyingObject, true));
        }
        return properties;
    }

    /*********************************************************
     *                     TreeElement                       *
     *********************************************************/

    @Override
    public boolean isLeaf() {
        return true;
    }

}
