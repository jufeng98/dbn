package com.dci.intellij.dbn.navigation.psi;

import com.dci.intellij.dbn.common.dispose.FailsafeUtil;
import com.dci.intellij.dbn.connection.GenericDatabaseElement;
import com.dci.intellij.dbn.language.common.psi.EmptySearchScope;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.list.DBObjectList;
import com.dci.intellij.dbn.object.common.property.DBObjectProperty;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.dci.intellij.dbn.vfs.DBVirtualFile;
import com.dci.intellij.dbn.vfs.DatabaseFileSystem;
import com.dci.intellij.dbn.vfs.DatabaseFileViewProvider;
import com.intellij.lang.FileASTNode;
import com.intellij.lang.Language;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.UnknownFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiInvalidElementAccessException;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DBObjectPsiFile implements PsiFile, Disposable {
    private DBObjectRef objectRef;

    public DBObjectPsiFile(DBObjectRef objectRef) {
        this.objectRef = objectRef;
    }

    @NotNull
    public DBObject getObject() {
        return FailsafeUtil.get(objectRef.get());
    }

    @Override
    public void dispose() {
    }


    /*********************************************************
     *                      PsiElement                       *
     *********************************************************/
    @NotNull
    public String getName() {
        return objectRef.getObjectName();
    }

    public ItemPresentation getPresentation() {
        return getObject();
    }

    public FileStatus getFileStatus() {
        return FileStatus.NOT_CHANGED;
    }

    @NotNull
    public Project getProject() throws PsiInvalidElementAccessException {
        return getObject().getProject();
    }

    @NotNull
    public Language getLanguage() {
        return Language.ANY;
    }

    public PsiDirectory getParent() {
        DBObject object = getObject();
        GenericDatabaseElement parent = object.getParent();
        if (parent instanceof DBObjectList) {
            DBObjectList objectList = (DBObjectList) parent;
            return objectList.getPsiDirectory();
        }
        return null;
    }

    public FileASTNode getNode() {
        return null;
    }

    public void navigate(boolean requestFocus) {
        DBObject object = getObject();
        if (object.is(DBObjectProperty.EDITABLE)) {
            DatabaseFileSystem.getInstance().openEditor(object, requestFocus);
        } else {
            object.navigate(requestFocus);
        }
    }

    public boolean canNavigate() {
        return true;
    }

    public boolean canNavigateToSource() {
        return false;
    }

    public PsiManager getManager() {
        return PsiManager.getInstance(getProject());
    }

    @NotNull
    public PsiElement[] getChildren() {
        return new PsiElement[0];
    }

    public PsiElement getFirstChild() {
        return null;
    }

    public PsiElement getLastChild() {
        return null;
    }

    public PsiElement getNextSibling() {
        return null;
    }

    public PsiElement getPrevSibling() {
        return null;
    }

    public PsiFile getContainingFile() throws PsiInvalidElementAccessException {
        return this;
    }

    public TextRange getTextRange() {
        return null;
    }

    public int getStartOffsetInParent() {
        return 0;
    }

    public int getTextLength() {
        return 0;
    }

    public PsiElement findElementAt(int offset) {
        return null;
    }

    public PsiReference findReferenceAt(int offset) {
        return null;
    }

    public int getTextOffset() {
        return 0;
    }

    public String getText() {
        return null;
    }

    @NotNull
    public char[] textToCharArray() {
        return new char[0];
    }

    public PsiElement getNavigationElement() {
        return this;
    }

    public PsiElement getOriginalElement() {
        return this;
    }

    public boolean textMatches(@NotNull CharSequence text) {
        return false;
    }

    public boolean textMatches(@NotNull PsiElement element) {
        return false;
    }

    public boolean textContains(char c) {
        return false;
    }

    public void accept(@NotNull PsiElementVisitor visitor) {

    }

    public void acceptChildren(@NotNull PsiElementVisitor visitor) {

    }

    public PsiElement copy() {
        return null;
    }

    public PsiElement add(@NotNull PsiElement element) throws IncorrectOperationException {
        throw new IncorrectOperationException("Operation not supported");
    }

    public PsiElement addBefore(@NotNull PsiElement element, PsiElement anchor) throws IncorrectOperationException {
        throw new IncorrectOperationException("Operation not supported");
    }

    public PsiElement addAfter(@NotNull PsiElement element, PsiElement anchor) throws IncorrectOperationException {
        throw new IncorrectOperationException("Operation not supported");
    }

    public void checkAdd(@NotNull PsiElement element) throws IncorrectOperationException {
        throw new IncorrectOperationException("Operation not supported");
    }

    public PsiElement addRange(PsiElement first, PsiElement last) throws IncorrectOperationException {
        throw new IncorrectOperationException("Operation not supported");
    }

    public PsiElement addRangeBefore(@NotNull PsiElement first, @NotNull PsiElement last, PsiElement anchor) throws IncorrectOperationException {
        throw new IncorrectOperationException("Operation not supported");
    }

    public PsiElement addRangeAfter(PsiElement first, PsiElement last, PsiElement anchor) throws IncorrectOperationException {
        throw new IncorrectOperationException("Operation not supported");
    }

    public void delete() throws IncorrectOperationException {

    }

    public void checkDelete() throws IncorrectOperationException {

    }

    public void deleteChildRange(PsiElement first, PsiElement last) throws IncorrectOperationException {

    }

    public PsiElement replace(@NotNull PsiElement newElement) throws IncorrectOperationException {
        return null;
    }

    public boolean isValid() {
        return true;
    }

    public boolean isWritable() {
        return false;
    }

    public PsiReference getReference() {
        return null;
    }

    @NotNull
    public PsiReference[] getReferences() {
        return new PsiReference[0];
    }

    public <T> T getCopyableUserData(Key<T> key) {
        return null;
    }

    public <T> void putCopyableUserData(Key<T> key, T value) {

    }

    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place) {
        return false;
    }

    public PsiElement getContext() {
        return null;
    }

    public boolean isPhysical() {
        return true;
    }

    @NotNull
    public GlobalSearchScope getResolveScope() {
        return EmptySearchScope.INSTANCE;
    }

    @NotNull
    public SearchScope getUseScope() {
        return EmptySearchScope.INSTANCE;
    }

    public boolean isEquivalentTo(PsiElement another) {
        return false;
    }

    public Icon getIcon(int flags) {
        return getObject().getIcon();
    }

    public <T> T getUserData(@NotNull Key<T> key) {
        return null;
    }

    public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {

    }

    /*********************************************************
     *                        PsiFile                        *
     *********************************************************/
    @NotNull
    public VirtualFile getVirtualFile() {
        return getObject().getVirtualFile();
    }

    public boolean processChildren(PsiElementProcessor<PsiFileSystemItem> processor) {
        return true;
    }

    @NotNull
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        throw new IncorrectOperationException("Operation not supported");
    }

    public boolean isDirectory() {
        return false;
    }

    public PsiDirectory getContainingDirectory() {
        return getParent();
    }

    public long getModificationStamp() {
        return 0;
    }

    @NotNull
    public PsiFile getOriginalFile() {
        return this;
    }

    @NotNull
    public FileType getFileType() {
        return UnknownFileType.INSTANCE;
    }

    @NotNull
    public PsiFile[] getPsiRoots() {
        return new PsiFile[0];
    }

    @NotNull
    public FileViewProvider getViewProvider() {
        DBVirtualFile virtualFile = (DBVirtualFile) getVirtualFile();
        DatabaseFileViewProvider viewProvider = virtualFile.getUserData(DatabaseFileViewProvider.CACHED_VIEW_PROVIDER);
        if (viewProvider == null) {
            viewProvider = new DatabaseFileViewProvider(PsiManager.getInstance(getProject()), getVirtualFile(), true);
        }
        return viewProvider;
    }

    public void subtreeChanged() {

    }

    public void checkSetName(String name) throws IncorrectOperationException {

    }
}
