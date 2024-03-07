package com.dbn.navigation.psi;

import com.dbn.common.dispose.Failsafe;
import com.dbn.connection.DatabaseEntity;
import com.dbn.editor.DatabaseFileEditorManager;
import com.dbn.language.common.psi.EmptySearchScope;
import com.dbn.navigation.ReadonlyPsiStub;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.list.DBObjectList;
import com.dbn.object.common.property.DBObjectProperty;
import com.dbn.object.lookup.DBObjectRef;
import com.dbn.vfs.DBVirtualFile;
import com.dbn.vfs.DatabaseFileViewProvider;
import com.intellij.lang.FileASTNode;
import com.intellij.lang.Language;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.UnknownFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.dbn.common.dispose.Failsafe.guarded;

public class DBObjectPsiFile extends UserDataHolderBase implements PsiFile, Disposable, ReadonlyPsiStub {
    private final DBObjectRef<?> objectRef;

    public DBObjectPsiFile(DBObjectRef<?> objectRef) {
        this.objectRef = objectRef;
    }

    @NotNull
    public DBObject getObject() {
        return Failsafe.nn(objectRef.get());
    }

    @Override
    public void dispose() {
    }


    /*********************************************************
     *                      PsiElement                       *
     *********************************************************/
    @Override
    @NotNull
    public String getName() {
        return objectRef.getObjectName();
    }

    @Override
    public ItemPresentation getPresentation() {
        return getObject();
    }

    public FileStatus getFileStatus() {
        return FileStatus.NOT_CHANGED;
    }

    @Override
    @NotNull
    public Project getProject() throws PsiInvalidElementAccessException {
        return getObject().getProject();
    }

    @Override
    @NotNull
    public Language getLanguage() {
        return Language.ANY;
    }

    @Override
    public PsiDirectory getParent() {
        DBObject object = getObject();
        DatabaseEntity parent = object.getParent();
        if (parent instanceof DBObjectList) {
            DBObjectList<?> objectList = (DBObjectList<?>) parent;
            return objectList.getPsiDirectory();
        }
        return null;
    }

    @Override
    public FileASTNode getNode() {
        return null;
    }

    @Override
    public void navigate(boolean requestFocus) {
        guarded(this, f -> f.navigateToObject(requestFocus));;
    }

    private void navigateToObject(boolean requestFocus) {
        DBObject object = getObject();
        if (object.is(DBObjectProperty.EDITABLE)) {
            DatabaseFileEditorManager editorManager = DatabaseFileEditorManager.getInstance(getProject());
            editorManager.connectAndOpenEditor(object, null, false, requestFocus);
        } else {
            object.navigate(requestFocus);
        }
    }

    @Override
    public boolean canNavigate() {
        return true;
    }

    @Override
    public boolean canNavigateToSource() {
        return false;
    }

    @Override
    public PsiManager getManager() {
        return PsiManager.getInstance(getProject());
    }

    @Override
    @NotNull
    public PsiElement[] getChildren() {
        return new PsiElement[0];
    }

    @Override
    public PsiElement getFirstChild() {
        return null;
    }

    @Override
    public PsiElement getLastChild() {
        return null;
    }

    @Override
    public PsiElement getNextSibling() {
        return null;
    }

    @Override
    public PsiElement getPrevSibling() {
        return null;
    }

    @Override
    public PsiFile getContainingFile() throws PsiInvalidElementAccessException {
        return this;
    }

    @Override
    public TextRange getTextRange() {
        return null;
    }

    @Override
    public int getStartOffsetInParent() {
        return 0;
    }

    @Override
    public int getTextLength() {
        return 0;
    }

    @Override
    public PsiElement findElementAt(int offset) {
        return null;
    }

    @Override
    public PsiReference findReferenceAt(int offset) {
        return null;
    }

    @Override
    public int getTextOffset() {
        return 0;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    @NotNull
    public char[] textToCharArray() {
        return new char[0];
    }

    @Override
    public PsiElement getNavigationElement() {
        return this;
    }

    @Override
    public PsiElement getOriginalElement() {
        return this;
    }

    @Override
    public boolean textMatches(@NotNull CharSequence text) {
        return false;
    }

    @Override
    public boolean textMatches(@NotNull PsiElement element) {
        return false;
    }

    @Override
    public boolean textContains(char c) {
        return false;
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {

    }

    @Override
    public void acceptChildren(@NotNull PsiElementVisitor visitor) {

    }

    @Override
    public PsiElement copy() {
        return null;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isWritable() {
        return false;
    }

    @Override
    public PsiReference getReference() {
        return null;
    }

    @Override
    @NotNull
    public PsiReference[] getReferences() {
        return new PsiReference[0];
    }

    @Override
    public <T> T getCopyableUserData(@NotNull Key<T> key) {
        return null;
    }

    @Override
    public <T> void putCopyableUserData(@NotNull Key<T> key, T value) {

    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place) {
        return false;
    }

    @Override
    public PsiElement getContext() {
        return null;
    }

    @Override
    public boolean isPhysical() {
        return true;
    }

    @Override
    @NotNull
    public GlobalSearchScope getResolveScope() {
        return EmptySearchScope.INSTANCE;
    }

    @Override
    @NotNull
    public SearchScope getUseScope() {
        return EmptySearchScope.INSTANCE;
    }

    @Override
    public boolean isEquivalentTo(PsiElement another) {
        return false;
    }

    @Override
    public Icon getIcon(int flags) {
        return getObject().getIcon();
    }

    @Override
    public <T> T getUserData(@NotNull Key<T> key) {
        return null;
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {

    }

    /*********************************************************
     *                        PsiFile                        *
     *********************************************************/
    @Override
    @NotNull
    public VirtualFile getVirtualFile() {
        return getObject().getVirtualFile();
    }

    @Override
    public boolean processChildren(@NotNull PsiElementProcessor processor) {
        return false;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public PsiDirectory getContainingDirectory() {
        return getParent();
    }

    @Override
    public long getModificationStamp() {
        return 0;
    }

    @Override
    @NotNull
    public PsiFile getOriginalFile() {
        return this;
    }

    @Override
    @NotNull
    public FileType getFileType() {
        return UnknownFileType.INSTANCE;
    }

    @Override
    @NotNull
    public PsiFile[] getPsiRoots() {
        return new PsiFile[] {this};
    }

    @Override
    @NotNull
    public FileViewProvider getViewProvider() {
        DBVirtualFile virtualFile = (DBVirtualFile) getVirtualFile();
        DatabaseFileViewProvider viewProvider = virtualFile.getCachedViewProvider();
        if (viewProvider == null) {
            viewProvider = new DatabaseFileViewProvider(getProject(), getVirtualFile(), true);
        }
        return viewProvider;
    }

    @Override
    public void subtreeChanged() {

    }

    @Override
    public void checkSetName(String name) throws IncorrectOperationException {

    }
}
