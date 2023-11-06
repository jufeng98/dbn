package com.dci.intellij.dbn.object.common;

import com.dci.intellij.dbn.browser.model.BrowserTreeNode;
import com.dci.intellij.dbn.code.common.lookup.LookupItemBuilderProvider;
import com.dci.intellij.dbn.common.Referenceable;
import com.dci.intellij.dbn.common.content.DynamicContentElement;
import com.dci.intellij.dbn.common.content.DynamicContentType;
import com.dci.intellij.dbn.common.dispose.UnlistedDisposable;
import com.dci.intellij.dbn.common.environment.EnvironmentTypeProvider;
import com.dci.intellij.dbn.common.property.PropertyHolder;
import com.dci.intellij.dbn.common.routine.Consumer;
import com.dci.intellij.dbn.common.ui.Presentable;
import com.dci.intellij.dbn.connection.ConnectionContext;
import com.dci.intellij.dbn.connection.ConnectionId;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.language.common.DBLanguage;
import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.object.DBUser;
import com.dci.intellij.dbn.object.common.list.DBObjectList;
import com.dci.intellij.dbn.object.common.list.DBObjectListContainer;
import com.dci.intellij.dbn.object.common.list.DBObjectListVisitor;
import com.dci.intellij.dbn.object.common.list.DBObjectNavigationList;
import com.dci.intellij.dbn.object.common.operation.DBOperationExecutor;
import com.dci.intellij.dbn.object.common.property.DBObjectProperty;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.dci.intellij.dbn.object.properties.PresentableProperty;
import com.dci.intellij.dbn.object.type.DBObjectType;
import com.dci.intellij.dbn.vfs.file.DBObjectVirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public interface DBObject extends
        PropertyHolder<DBObjectProperty>,
        BrowserTreeNode,
        DynamicContentElement,
        LookupItemBuilderProvider,
        Referenceable,
        EnvironmentTypeProvider,
        Presentable,
        UnlistedDisposable {

    @NotNull
    @Override
    String getName();

    @NotNull
    @Override
    ConnectionId getConnectionId();

    @NotNull
    DBObjectType getObjectType();

    boolean isOfType(DBObjectType objectType);

    DBLanguageDialect getLanguageDialect(DBLanguage language);
    
    DBObjectAttribute[] getObjectAttributes();
    DBObjectAttribute getNameAttribute();

    String getQuotedName(boolean quoteAlways);
    boolean needsNameQuoting();
    String getQualifiedNameWithType();
    String getNavigationTooltipText();
    String getTypeName();
    @Override
    @Nullable
    Icon getIcon();
    Icon getOriginalIcon();
    DBContentType getContentType();

    @Nullable
    DBUser getOwner();

    <T extends DBObject> T getParentObject();

    <T extends DBObject> DBObjectRef<T> getParentObjectRef();

    @Nullable
    DBObject getDefaultNavigationObject();

    <T extends DBObject> List<T> getChildObjects(DBObjectType objectType);

    @Nullable
    <T extends DBObject> T getChildObject(DBObjectType objectType, String name);

    @Nullable
    <T extends DBObject> T getChildObject(DBObjectType objectType, String name, short overload);

    @NotNull
    List<DBObject> collectChildObjects(DBObjectType objectType);

    void collectChildObjects(DBObjectType objectType, Consumer<? super DBObject> consumer);

    @Nullable
    <T extends DBObject> DBObjectList<T> getChildObjectList(DBObjectType objectType);

    <T extends DBObject> T getChildObject(String name, short overload);

    <T extends DBObject> T getChildObject(DBObjectType type, String name, boolean lookupHidden);

    <T extends DBObject> T getChildObject(DBObjectType type, String name, short overload, boolean lookupHidden);

    <T extends DBObject> T getChildObjectNoLoad(String objectName);

    List<String> getChildObjectNames(DBObjectType objectType);

    List<DBObjectNavigationList> getNavigationLists();

    boolean isEditorReady();

    void makeEditorReady();

    @Nullable
    DBObjectListContainer getChildObjects();

    void visitChildObjects(DBObjectListVisitor visitor, boolean visitInternal);

    DBOperationExecutor getOperationExecutor();

    @NotNull
    DBObjectVirtualFile getVirtualFile();

    List<PresentableProperty> getPresentableProperties();

    @Override
    DBObjectRef ref();

    boolean isValid();

    boolean isVirtual();

    boolean isEditable();

    boolean isParentOf(DBObject object);

    void refresh(DBObjectType objectType);

    @Override
    default DynamicContentType<?> getDynamicContentType() {
        return getObjectType();
    }

    @Deprecated // do not use schema aware context
    default ConnectionContext createConnectionContext() {
        return new ConnectionContext(getProject(), getConnectionId(), getSchemaId());
    }
}
