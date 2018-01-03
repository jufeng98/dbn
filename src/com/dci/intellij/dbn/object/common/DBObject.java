package com.dci.intellij.dbn.object.common;

import javax.swing.Icon;
import java.sql.SQLException;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.dci.intellij.dbn.browser.model.BrowserTreeNode;
import com.dci.intellij.dbn.code.common.lookup.LookupItemBuilderProvider;
import com.dci.intellij.dbn.common.Referenceable;
import com.dci.intellij.dbn.common.content.DynamicContentElement;
import com.dci.intellij.dbn.common.environment.EnvironmentTypeProvider;
import com.dci.intellij.dbn.common.property.PropertyHolder;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.PresentableConnectionProvider;
import com.dci.intellij.dbn.language.common.DBLanguage;
import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.DBUser;
import com.dci.intellij.dbn.object.common.list.DBObjectList;
import com.dci.intellij.dbn.object.common.list.DBObjectListContainer;
import com.dci.intellij.dbn.object.common.list.DBObjectNavigationList;
import com.dci.intellij.dbn.object.common.list.DBObjectRelationListContainer;
import com.dci.intellij.dbn.object.common.operation.DBOperationExecutor;
import com.dci.intellij.dbn.object.common.property.DBObjectProperty;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.dci.intellij.dbn.object.properties.PresentableProperty;
import com.dci.intellij.dbn.vfs.DBObjectVirtualFile;
import com.intellij.psi.PsiElement;

public interface DBObject extends PropertyHolder<DBObjectProperty>, BrowserTreeNode, DynamicContentElement, LookupItemBuilderProvider, Referenceable, EnvironmentTypeProvider, PresentableConnectionProvider {
    DBObjectType getObjectType();
    boolean isOfType(DBObjectType objectType);

    DBLanguageDialect getLanguageDialect(DBLanguage language);
    
    DBObjectAttribute[] getObjectAttributes();
    DBObjectAttribute getNameAttribute();

    @NotNull
    String getName();
    int getOverload();
    String getQuotedName(boolean quoteAlways);
    boolean needsNameQuoting();
    String getQualifiedName();
    String getQualifiedNameWithType();
    String getNavigationTooltipText();
    String getTypeName();
    @Nullable
    Icon getIcon();
    Icon getOriginalIcon();

    @Nullable
    DBUser getOwner();
    DBSchema getSchema();

    @NotNull
    @Override
    ConnectionHandler getConnectionHandler();

    DBObject getParentObject();

    @NotNull DBObjectBundle getObjectBundle();
    @Nullable DBObject getDefaultNavigationObject();

    @NotNull
    List<DBObject> getChildObjects(DBObjectType objectType);

    @Nullable
    DBObjectList<? extends DBObject> getChildObjectList(DBObjectType objectType);

    DBObject getChildObject(DBObjectType objectType, String name, boolean lookupHidden);

    DBObject getChildObject(DBObjectType objectType, String name, int overload, boolean lookupHidden);

    @Nullable
    DBObject getChildObject(String name, boolean lookupHidden);

    @Nullable
    DBObject getChildObject(String name, int overload, boolean lookupHidden);

    List<DBObjectNavigationList> getNavigationLists();

    @Nullable
    DBObjectListContainer getChildObjects();

    @Nullable
    DBObjectRelationListContainer getChildObjectRelations();
    String extractDDL() throws SQLException;

    @Nullable
    DBObject getUndisposedElement();

    DBOperationExecutor getOperationExecutor();

    @NotNull
    DBObjectVirtualFile getVirtualFile();
    List<PresentableProperty> getPresentableProperties();
    DBObjectRef getRef();

    boolean isValid();

    PsiElement getPsi();

    boolean isParentOf(DBObject object);

    @NotNull
    @Override
    BrowserTreeNode getParent();
}
