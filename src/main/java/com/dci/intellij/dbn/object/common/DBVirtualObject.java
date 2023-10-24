package com.dci.intellij.dbn.object.common;

import com.dci.intellij.dbn.code.common.lookup.LookupItemBuilder;
import com.dci.intellij.dbn.code.common.lookup.ObjectLookupItemBuilder;
import com.dci.intellij.dbn.common.dispose.Disposer;
import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.latent.Latent;
import com.dci.intellij.dbn.common.ref.WeakRef;
import com.dci.intellij.dbn.common.routine.Consumer;
import com.dci.intellij.dbn.common.util.Lists;
import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionId;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.dci.intellij.dbn.database.common.metadata.DBObjectMetadata;
import com.dci.intellij.dbn.language.common.DBLanguage;
import com.dci.intellij.dbn.language.common.DBLanguagePsiFile;
import com.dci.intellij.dbn.language.common.PsiElementRef;
import com.dci.intellij.dbn.language.common.element.util.ElementTypeAttribute;
import com.dci.intellij.dbn.language.common.element.util.IdentifierCategory;
import com.dci.intellij.dbn.language.common.psi.BasePsiElement;
import com.dci.intellij.dbn.language.common.psi.IdentifierPsiElement;
import com.dci.intellij.dbn.language.common.psi.LeafPsiElement;
import com.dci.intellij.dbn.language.common.psi.QualifiedIdentifierPsiElement;
import com.dci.intellij.dbn.language.common.psi.lookup.*;
import com.dci.intellij.dbn.object.common.list.DBObjectList;
import com.dci.intellij.dbn.object.common.list.DBObjectListContainer;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.dci.intellij.dbn.object.type.DBObjectType;
import com.dci.intellij.dbn.vfs.file.DBContentVirtualFile;
import com.intellij.ide.util.EditSourceUtil;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiInvalidElementAccessException;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.dci.intellij.dbn.common.content.DynamicContentProperty.*;
import static com.dci.intellij.dbn.common.dispose.Failsafe.nd;
import static com.dci.intellij.dbn.common.dispose.Failsafe.nn;
import static com.dci.intellij.dbn.common.util.Documents.getDocument;
import static com.dci.intellij.dbn.common.util.Documents.getEditors;
import static com.dci.intellij.dbn.object.common.sorting.DBObjectComparator.compareName;
import static com.dci.intellij.dbn.object.common.sorting.DBObjectComparator.compareType;

public class DBVirtualObject extends DBRootObjectImpl implements PsiReference {
    private static final PsiLookupAdapter CHR_STAR_LOOKUP_ADAPTER = new TokenTypeLookupAdapter(element -> element.getLanguage().getSharedTokenTypes().getChrStar());
    private static final PsiLookupAdapter COL_INDEX_LOOKUP_ADAPTER = new TokenTypeLookupAdapter(element -> element.getLanguage().getSharedTokenTypes().getInteger());
    private static final ObjectReferenceLookupAdapter DATASET_LOOKUP_ADAPTER = new ObjectReferenceLookupAdapter(null, DBObjectType.DATASET, null);

    private volatile boolean loadingChildren;
    private PsiElementRef<BasePsiElement> relevantPsiElement;
    private final DBObjectPsiCache psiCache;
    private final Map<String, ObjectLookupItemBuilder> lookupItemBuilder = new ConcurrentHashMap<>();

    private final Latent<Boolean> valid = Latent.timed(10, TimeUnit.SECONDS, () -> {
        boolean valid = Failsafe.guarded(true, this, o -> o.checkValid());
        if (!valid) Disposer.dispose(this);
        return valid;
    });

    private boolean checkValid() {
        if (isDisposed()) return false;

        BasePsiElement underlyingPsiElement = getUnderlyingPsiElement();
        if (underlyingPsiElement == null) return false;

        boolean psiElementValid = underlyingPsiElement.isValid();
        if (!psiElementValid) return false;

        DBObjectType objectType = getObjectType();
        if (objectType.matches(DBObjectType.DATASET) || objectType.matches(DBObjectType.TYPE)) return true; // no special checks

        BasePsiElement relevantPsiElement = getRelevantPsiElement();
        if (!Strings.equalsIgnoreCase(getName(), relevantPsiElement.getText())) return false;

        if (relevantPsiElement instanceof IdentifierPsiElement) {
            IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) relevantPsiElement;
            return identifierPsiElement.getObjectType() == objectType;
        }
        return true;

    }

    public DBVirtualObject(@NotNull BasePsiElement psiElement) {
        super(
            psiElement.getConnection(),
            psiElement.getElementType().getVirtualObjectType(),
            psiElement.getText());

        psiCache = new DBObjectPsiCache(psiElement);
        relevantPsiElement = PsiElementRef.of(psiElement);
        String name = resolveName();
        ref = new DBObjectRef<>(this, name);
    }

    private String resolveName() {
        BasePsiElement psiElement = getRelevantPsiElement();
        DBObjectType objectType = getObjectType();
        switch (objectType) {
            case DATASET: return resolveDatasetName(psiElement);
            case COLUMN: return resolveColumnName(psiElement);
            case CURSOR:
            case TYPE:
            case TYPE_ATTRIBUTE: return resolveObjectName(psiElement);
        }
        return "";
    }

    private String resolveObjectName(@NotNull BasePsiElement psiElement) {
        BasePsiElement relevantPsiElement = psiElement.findFirstPsiElement(ElementTypeAttribute.SUBJECT);
        if (relevantPsiElement != null) {
            this.relevantPsiElement = PsiElementRef.of(relevantPsiElement);
            return relevantPsiElement.getText();
        }
        return "";
    }

    private String resolveDatasetName(@NotNull BasePsiElement psiElement) {
        if (psiElement instanceof LeafPsiElement) {
            LeafPsiElement leafPsiElement = (LeafPsiElement) psiElement;
            ObjectLookupAdapter lookupAdapter = new ObjectLookupAdapter(leafPsiElement, IdentifierCategory.REFERENCE, DBObjectType.DATASET);
            BasePsiElement dataset = lookupAdapter.findInParentScopeOf(psiElement);
            if (dataset != null) {
                this.relevantPsiElement = PsiElementRef.of(dataset);
                return dataset.getText();
            } else {
                return  "UNKNOWN";
            }
        }

        List<String> tableNames = new ArrayList<>();
        ObjectLookupAdapter lookupAdapter = new ObjectLookupAdapter(null, IdentifierCategory.REFERENCE, DBObjectType.DATASET);
        lookupAdapter.collectInElement(psiElement, basePsiElement -> {
            if (basePsiElement instanceof IdentifierPsiElement) {
                IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) basePsiElement;
                String tableName = identifierPsiElement.getText().toUpperCase();
                if (!tableNames.contains(tableName)) {
                    tableNames.add(tableName);
                }
            }
        });

        Collections.sort(tableNames);

        StringBuilder nameBuilder = new StringBuilder();
        for (CharSequence tableName : tableNames) {
            if (nameBuilder.length() > 0) nameBuilder.append(", ");
            nameBuilder.append(tableName);
        }

        return "subquery " + nameBuilder;
    }

    private String resolveColumnName(BasePsiElement psiElement) {
        PsiLookupAdapter lookupAdapter = LookupAdapters.alias(DBObjectType.COLUMN);
        BasePsiElement specificPsiElement = lookupAdapter.findInElement(psiElement);

        if (specificPsiElement == null) {
            lookupAdapter = LookupAdapters.object(DBObjectType.COLUMN);
            specificPsiElement = lookupAdapter.findInElement(psiElement);
        }

        if (specificPsiElement != null) {
            this.relevantPsiElement = PsiElementRef.of(specificPsiElement);
            return specificPsiElement.getText();
        }

        specificPsiElement = WeakRef.get(this.relevantPsiElement);
        if (specificPsiElement != null) {
            String text = specificPsiElement.getText();
            if (!text.contains("\\s*")) {
                return text.trim();
            }
        }
        return "";
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBObjectMetadata metadata) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public DBObjectPsiCache getPsiCache() {
        return psiCache;
    }

    @NotNull
    public LookupItemBuilder getLookupItemBuilder(DBLanguage language) {
        return lookupItemBuilder.computeIfAbsent(language.getID(), id -> new ObjectLookupItemBuilder(ref(), language));
    }

    @Override
    public boolean isValid() {
        if (isDisposed()) return false;
        return valid.get();
    }

    @Override
    public boolean isVirtual() {
        return true;
    }

    @Override
    @NotNull
    public List<DBObject> collectChildObjects(DBObjectType objectType) {
        return getChildObjects(objectType);
    }

    @Override
    public DBObject getChildObject(DBObjectType type, String name, short overload, boolean lookupHidden) {
        DBObjectList<DBObject> childObjectList = getChildObjectList(type);
        if (childObjectList != null) {
            DBObject object = childObjectList.getObject(name, overload);
            if (object != null) return object;
        }

        BasePsiElement relevantPsiElement = getRelevantPsiElement();
        DBObject underlyingObject = relevantPsiElement.getUnderlyingObject();
        if (underlyingObject != null && underlyingObject != this) {
            DBObject object = underlyingObject.getChildObject(type, name, overload, lookupHidden);
            if (object != null) return object;
        }

        return getChildObject(name, overload);
    }

    @Override
    public void collectChildObjects(DBObjectType objectType, Consumer consumer) {
        super.collectChildObjects(objectType, consumer);
        BasePsiElement relevantPsiElement = getRelevantPsiElement();
        DBObject underlyingObject = relevantPsiElement.getUnderlyingObject();
        if (underlyingObject != null && underlyingObject != this) {
            underlyingObject.collectChildObjects(objectType, consumer);
        }

    }

    @Override
    @Nullable
    public DBObjectList<DBObject> getChildObjectList(DBObjectType objectType) {
        if (loadingChildren) return null;

        synchronized (this) {
            if (loadingChildren) return null;

            try {
                loadingChildren = true;
                return loadChildObjectList(objectType);
            } finally {
                loadingChildren = false;
            }
        }
    }

    private DBObjectList<DBObject> loadChildObjectList(DBObjectType objectType) {
        DBObjectListContainer childObjects = ensureChildObjects();
        DBObjectList<DBObject> objectList = childObjects.getObjectList(objectType);

        if (objectList == null) {
            if (!objectType.isChildOf(getObjectType())) return null;

            objectList = childObjects.createObjectList(objectType, this, MUTABLE, VIRTUAL);
            if (objectList == null) return null; // not supported (?)

            loadChildObjects(objectType, objectList);
            objectList.set(LOADED, true);
        } else {
            // unloaded lists may be the result of ProcessCancelledExceptions during annotation processing (force reload)
            boolean invalid = !objectList.isLoaded() || Lists.anyMatch(objectList.getObjects(), o -> !o.isValid());
            if (!invalid) return objectList;

            objectList.setElements(Collections.emptyList()); // reset
            loadChildObjects(objectType, objectList);
            objectList.set(LOADED, true);
        }
        return objectList;
    }

    private void loadChildObjects(DBObjectType childObjectType, DBObjectList<DBObject> objectList) {
        BasePsiElement<?> underlyingPsiElement = getUnderlyingPsiElement();
        if (underlyingPsiElement == null) return;

        DBObjectType objectType = getObjectType();
        List<DBObject> objects = new ArrayList<>();
        PsiLookupAdapter lookupAdapter = LookupAdapters.virtualObject(objectType, childObjectType);
        underlyingPsiElement.collectPsiElements(lookupAdapter, 100, element -> {
            BasePsiElement child = (BasePsiElement) element;
            // handle STAR column
            if (childObjectType == DBObjectType.COLUMN) {
                LeafPsiElement starPsiElement = (LeafPsiElement) CHR_STAR_LOOKUP_ADAPTER.findInElement(child);
                if (starPsiElement != null) loadAllColumns(starPsiElement, objects);

                LeafPsiElement indexPsiElement = (LeafPsiElement) COL_INDEX_LOOKUP_ADAPTER.findInElement(child);
                if (indexPsiElement != null) loadColumns(indexPsiElement, objects);
            }

            DBObject object = child.getUnderlyingObject();
            if (object != null && Strings.isNotEmpty(object.getName()) && object.getObjectType().isChildOf(objectType) && !objectList.contains(object)) {
                if (object instanceof DBVirtualObject) {
                    DBVirtualObject virtualObject = (DBVirtualObject) object;
                    virtualObject.setParentObject(this);
                }
                objects.add(object);
            }

        });

        objectList.setElements(objects);
        objectList.set(MASTER, false);
    }

    private void loadAllColumns(LeafPsiElement starPsiElement, List<DBObject> objects) {
        if (starPsiElement.getParent() instanceof QualifiedIdentifierPsiElement) {
            QualifiedIdentifierPsiElement qualifiedIdentifierPsiElement = (QualifiedIdentifierPsiElement) starPsiElement.getParent();
            int index = qualifiedIdentifierPsiElement.getIndexOf(starPsiElement);
            if (index <= 0) return;

            IdentifierPsiElement parentPsiElement = qualifiedIdentifierPsiElement.getLeafAtIndex(index - 1);
            DBObject object = parentPsiElement.getUnderlyingObject();
            if (object != null && object.getObjectType().matches(DBObjectType.DATASET)) {
                List<DBObject> columns = object.collectChildObjects(DBObjectType.COLUMN);
                objects.addAll(columns);
            }
        } else {
            BasePsiElement underlyingPsiElement = nn(getUnderlyingPsiElement());
            DATASET_LOOKUP_ADAPTER.collectInElement(underlyingPsiElement, basePsiElement -> {
                DBObject object = basePsiElement.getUnderlyingObject();
                if (object != null && object != this && object.getObjectType().matches(DBObjectType.DATASET)) {
                    List<DBObject> columns = object.collectChildObjects(DBObjectType.COLUMN);
                    objects.addAll(columns);
                }
            });
        }
    }
    private void loadColumns(LeafPsiElement indexPsiElement, List<DBObject> objects) {
        BasePsiElement<?> columnPsiElement = indexPsiElement.findEnclosingVirtualObjectElement(DBObjectType.COLUMN);
        if (columnPsiElement == null) return;

        String text = columnPsiElement.getText();
        if (!Strings.isIndex(text)) return;

        int columnIndex = Integer.parseInt(text) -1 ; // switch from DB indexing to 0 based
        if (columnIndex < 0) return;

        if (indexPsiElement.getParent() instanceof QualifiedIdentifierPsiElement) {
            QualifiedIdentifierPsiElement qualifiedIdentifierPsiElement = (QualifiedIdentifierPsiElement) indexPsiElement.getParent();
            int index = qualifiedIdentifierPsiElement.getIndexOf(indexPsiElement);
            if (index <= 0) return;

            IdentifierPsiElement parentPsiElement = qualifiedIdentifierPsiElement.getLeafAtIndex(index - 1);
            DBObject object = parentPsiElement.getUnderlyingObject();
            if (object == null || object == this) return;
            if (!object.getObjectType().matches(DBObjectType.DATASET)) return;

            List<DBObject> columns = object.collectChildObjects(DBObjectType.COLUMN);
            if (columns.size() > columnIndex) objects.add(columns.get(columnIndex));
        } else {
            BasePsiElement underlyingPsiElement = nn(getUnderlyingPsiElement());
            DATASET_LOOKUP_ADAPTER.collectInElement(underlyingPsiElement, basePsiElement -> {
                DBObject object = basePsiElement.getUnderlyingObject();
                if (object == null || object == this) return;
                if (!object.getObjectType().matches(DBObjectType.DATASET)) return;

                List<DBObject> columns = object.collectChildObjects(DBObjectType.COLUMN);
                if (columns.size() > columnIndex) objects.add(columns.get(columnIndex));
            });
        }
    }

    @Override
    public String getQualifiedNameWithType() {
        return getName();
    }

    @Override
    @NotNull
    public ConnectionHandler getConnection() {
        BasePsiElement underlyingPsiElement = nd(getUnderlyingPsiElement());
        DBLanguagePsiFile file = underlyingPsiElement.getFile();
        ConnectionHandler connection = file.getConnection();
        if (connection == null) {
            ConnectionManager connectionManager = ConnectionManager.getInstance(getProject());
            return connectionManager.getConnectionBundle().getVirtualConnection(ConnectionId.VIRTUAL_ORACLE);
        }
        return connection;
    }

    public void setParentObject(DBVirtualObject virtualObject) {
        ref.setParent(DBObjectRef.of(virtualObject));
    }

    @Override
    @NotNull
    public Project getProject() {
        PsiElement underlyingPsiElement = nn(getUnderlyingPsiElement());
        return underlyingPsiElement.getProject();
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return ref.getObjectType();
    }

    @Override
    public void navigate(boolean requestFocus) {
        PsiFile containingFile = getContainingFile();
        if (containingFile == null) return;

        VirtualFile virtualFile = containingFile.getVirtualFile();
        BasePsiElement relevantPsiElement = getRelevantPsiElement();
        if (virtualFile instanceof DBContentVirtualFile) {
            Document document = getDocument(containingFile);
            if (document == null) return;

            Editor[] editors = getEditors(document);
            OpenFileDescriptor descriptor = (OpenFileDescriptor) EditSourceUtil.getDescriptor(relevantPsiElement);
            if (descriptor == null) return;

            descriptor.navigateIn(editors[0]);
        } else {
            relevantPsiElement.navigate(requestFocus);
        }
    }
    
    @Override
    public PsiFile getContainingFile() throws PsiInvalidElementAccessException {
        BasePsiElement relevantPsiElement = getRelevantPsiElement();
        return relevantPsiElement.isValid() ? relevantPsiElement.getContainingFile() : null;
    }

    /*********************************************************
     *                       PsiReference                    *
     *********************************************************/
    @NotNull
    @Override
    public PsiElement getElement() {
        return getRelevantPsiElement();
    }

    @NotNull
    @Override
    public TextRange getRangeInElement() {
        return new TextRange(0, getName().length());
    }

    @Override
    @Nullable
    public PsiElement resolve() {
        return getUnderlyingPsiElement();
    }

    @Nullable
    public BasePsiElement getUnderlyingPsiElement() {
        return (BasePsiElement) getPsiCache().getPsiElement();
    }

    @NotNull
    private BasePsiElement getRelevantPsiElement() {
        BasePsiElement basePsiElement = PsiElementRef.get(relevantPsiElement);
        return nn(basePsiElement);
    }

    @Override
    @NotNull
    public String getCanonicalText() {
        return getName();
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return null;
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        return null;
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        return getUnderlyingPsiElement() == element;
    }

    @Override
    @NotNull
    public Object[] getVariants() {
        return new Object[0];
    }

    @Override
    public boolean isSoft() {
        return false;
    }

    @Override
    public boolean canNavigateToSource() {
        return false;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        DBObject that = (DBObject) o;
        int result = compareType(this, that);
        if (result == 0) {
            return compareName(this, that);
        }
        return result;
    }
}
