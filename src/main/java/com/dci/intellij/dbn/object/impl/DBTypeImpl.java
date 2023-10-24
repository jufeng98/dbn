package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.browser.DatabaseBrowserUtils;
import com.dci.intellij.dbn.browser.model.BrowserTreeNode;
import com.dci.intellij.dbn.browser.ui.HtmlToolTipBuilder;
import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.data.type.DBDataType;
import com.dci.intellij.dbn.data.type.DBDataTypeDefinition;
import com.dci.intellij.dbn.data.type.DBNativeDataType;
import com.dci.intellij.dbn.database.common.metadata.def.DBDataTypeMetadata;
import com.dci.intellij.dbn.database.common.metadata.def.DBTypeMetadata;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.object.*;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.object.common.list.DBObjectListContainer;
import com.dci.intellij.dbn.object.common.list.DBObjectNavigationList;
import com.dci.intellij.dbn.object.common.property.DBObjectProperty;
import com.dci.intellij.dbn.object.common.status.DBObjectStatus;
import com.dci.intellij.dbn.object.filter.type.ObjectTypeFilterSettings;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.dci.intellij.dbn.object.properties.DBDataTypePresentableProperty;
import com.dci.intellij.dbn.object.properties.PresentableProperty;
import com.dci.intellij.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.dci.intellij.dbn.object.type.DBObjectType.*;

class DBTypeImpl
        extends DBProgramImpl<DBTypeMetadata, DBTypeProcedure, DBTypeFunction, DBType>
        implements DBType {

    private String superTypeOwner;
    private String superTypeName;
    private DBObjectRef<DBType> superType;

    private DBDataTypeDefinition collectionElementTypeRef;
    private DBDataType collectionElementType;

    private DBNativeDataType nativeDataType;

    DBTypeImpl(DBSchemaObject parent, DBTypeMetadata metadata) throws SQLException {
        // type functions are not editable independently
        super(parent, metadata);
        assert this.getClass() != DBTypeImpl.class;
    }

    DBTypeImpl(DBSchema schema, DBTypeMetadata metadata) throws SQLException {
        super(schema, metadata);
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBTypeMetadata metadata) throws SQLException {
        String name = metadata.getTypeName();
        superTypeOwner = metadata.getSupertypeOwner();
        superTypeName = metadata.getSupertypeName();

        String typeCode = metadata.getTypeCode();
        boolean collection = metadata.isCollection();
        set(DBObjectProperty.COLLECTION, collection);

        nativeDataType = connection.getObjectBundle().getNativeDataType(typeCode);
        if (collection) {
            DBDataTypeMetadata collectionMetadata = metadata.getDataType().collection();
            collectionElementTypeRef = new DBDataTypeDefinition(collectionMetadata);
        }
        return name;
    }


    @Override
    protected void initLists(ConnectionHandler connection) {
        super.initLists(connection);
        if (!isCollection()) {
            DBObjectListContainer childObjects = ensureChildObjects();
            DBSchema schema = getSchema();
            childObjects.createSubcontentObjectList(TYPE_ATTRIBUTE, this, schema);
            childObjects.createSubcontentObjectList(TYPE_PROCEDURE, this, schema);
            childObjects.createSubcontentObjectList(TYPE_FUNCTION, this, schema);
            childObjects.createSubcontentObjectList(TYPE_TYPE, this, schema);
        }
    }

    @Override
    protected DBObjectType getFunctionObjectType() {
        return TYPE_FUNCTION;
    }

    @Override
    protected DBObjectType getProcedureObjectType() {
        return TYPE_PROCEDURE;
    }

    @Override
    protected DBObjectType getTypeObjectType() {
        return TYPE_TYPE;
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return TYPE;
    }

    @Override
    @Nullable
    public Icon getIcon() {
        if (getStatus().is(DBObjectStatus.VALID)) {
            if (getStatus().is(DBObjectStatus.DEBUG))  {
                return Icons.DBO_TYPE_DEBUG;
            } else {
                return isCollection() ? Icons.DBO_TYPE_COLLECTION : Icons.DBO_TYPE;
            }
        } else {
            return isCollection() ? Icons.DBO_TYPE_COLLECTION_ERR : Icons.DBO_TYPE_ERR;
        }
    }

    @Override
    public Icon getOriginalIcon() {
        return isCollection() ? Icons.DBO_TYPE_COLLECTION : Icons.DBO_TYPE;
    }

    @Override
    public List<DBTypeAttribute> getAttributes() {
        return getChildObjects(TYPE_ATTRIBUTE);
    }

    @Override
    public DBType getSuperType() {
        if (superType == null && superTypeOwner != null && superTypeName != null) {
            DBSchema schema = getObjectBundle().getSchema(superTypeOwner);
            DBType type = schema == null ? null : schema.getType(superTypeName);
            superType = DBObjectRef.of(type);
            superTypeOwner = null;
            superTypeName = null;
        }
        return DBObjectRef.get(superType);
    }

    @Override
    public DBDataType getCollectionElementType() {
        if (collectionElementType == null && collectionElementTypeRef != null) {
            collectionElementType = getObjectBundle().getDataTypes().getDataType(collectionElementTypeRef);
            collectionElementTypeRef = null;
        }
        return collectionElementType;
    }

    @Override
    @Nullable
    public DBObject getDefaultNavigationObject() {
        if (isCollection()) {
            DBDataType dataType = getCollectionElementType();
            if (dataType != null && dataType.isDeclared()) {
                return dataType.getDeclaredType();
            }

        }
        return null;
    }

    @Override
    public List<PresentableProperty> getPresentableProperties() {
        List<PresentableProperty> properties = super.getPresentableProperties();
        DBDataType collectionElementType = getCollectionElementType();
        if (collectionElementType != null) {
            properties.add(0, new DBDataTypePresentableProperty("Collection element type", collectionElementType));
        }
        return properties;
    }

    @Override
    public DBNativeDataType getNativeDataType() {
        return nativeDataType;
    }

    @Override
    public boolean isCollection() {
        return is(DBObjectProperty.COLLECTION);
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
        return isCollection() ?
                EMPTY_TREE_NODE_LIST :
                DatabaseBrowserUtils.createList(
                        getChildObjectList(TYPE_ATTRIBUTE),
                        getChildObjectList(TYPE_PROCEDURE),
                        getChildObjectList(TYPE_FUNCTION),
                        getChildObjectList(TYPE_TYPE));
    }

    @Override
    public boolean hasVisibleTreeChildren() {
        if (isCollection()) return false;

        ObjectTypeFilterSettings settings = getObjectTypeFilterSettings();
        return
            settings.isVisible(ATTRIBUTE) ||
            settings.isVisible(PROCEDURE) ||
            settings.isVisible(FUNCTION);
    }

    @Override
    public String getCodeParseRootId(DBContentType contentType) {
        if (getParentObject() instanceof DBSchema) {
            return contentType == DBContentType.CODE_SPEC ? "type_spec" :
                   contentType == DBContentType.CODE_BODY ? "type_body" : null;
        }
        return null;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if (o instanceof DBType) {
            DBType that = (DBType) o;
            if (Objects.equals(this.getParentObject(), that.getParentObject())) {
                return this.isCollection() == that.isCollection() ?
                        super.compareTo(o) :
                        that.isCollection() ? -1 : 1;
            }
        }
        return super.compareTo(o);
    }

    @Override
    protected @Nullable List<DBObjectNavigationList> createNavigationLists() {
        List<DBObjectNavigationList> navigationLists = new LinkedList<>();

        DBType superType = getSuperType();
        if (superType != null) {
            navigationLists.add(DBObjectNavigationList.create("Super Type", superType));
        }
        List<DBObject> types = getChildObjects(TYPE_TYPE);
        if (!types.isEmpty()) {
            navigationLists.add(DBObjectNavigationList.create("Sub Types", types));
        }
        if (isCollection()) {
            DBDataType dataType = getCollectionElementType();
            if (dataType != null && dataType.isDeclared()) {
                DBType collectionElementType = dataType.getDeclaredType();
                if (collectionElementType != null) {
                    navigationLists.add(DBObjectNavigationList.create("Collection element type", collectionElementType));
                }
            }
        }

        return navigationLists;
    }
}
