package com.dbn.navigation.psi;

import com.dbn.common.dispose.Disposer;
import com.dbn.common.dispose.Failsafe;
import com.dbn.connection.ConnectionHandler;
import com.dbn.language.common.psi.EmptySearchScope;
import com.dbn.object.common.list.DBObjectListContainer;
import com.dbn.vfs.file.DBConnectionVirtualFile;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnectionPsiDirectory implements PsiDirectory, Disposable {
    private DBConnectionVirtualFile virtualFile;

    public DBConnectionPsiDirectory(ConnectionHandler connection) {
        virtualFile = new DBConnectionVirtualFile(connection);
    }

    @Override
    @NotNull
    public DBConnectionVirtualFile getVirtualFile() {
        return Failsafe.nn(virtualFile);
    }

    @NotNull
    public ConnectionHandler getConnection() {
        return getVirtualFile().getConnection();
    }

    @Override
    @NotNull
    public String getName() {
        return getConnection().getName();
    }

    @Override
    public ItemPresentation getPresentation() {
        return getConnection().getObjectBundle();
    }

    public FileStatus getFileStatus() {
        return FileStatus.NOT_CHANGED;
    }

    @Override
    public void dispose() {
        Disposer.dispose(virtualFile);
        virtualFile = null;
    }

    @Override
    public boolean processChildren(@NotNull PsiElementProcessor processor) {
        return false;
    }

    @Override
    @NotNull
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        throw new IncorrectOperationException("Operation not supported");
    }

    @Override
    public PsiDirectory getParentDirectory() {
        return null;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    @NotNull
    public Project getProject() throws PsiInvalidElementAccessException {
        return Failsafe.nn(getVirtualFile().getProject());
    }

    @Override
    @NotNull
    public Language getLanguage() {
        return Language.ANY;
    }

    @Override
    public PsiManager getManager() {
        return PsiManager.getInstance(getProject());
    }

    @Override
    @NotNull
    public PsiElement[] getChildren() {
        List<PsiElement> children = new ArrayList<>();
        DBObjectListContainer objectLists = virtualFile.getConnection().getObjectBundle().getObjectLists();
        objectLists.visit(o -> children.add(o.getPsiDirectory()), false);
        return children.toArray(new PsiElement[0]);
    }

    @Override
    public PsiDirectory getParent() {
        return null;
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
        return null;
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
    public PsiElement add(@NotNull PsiElement element) throws IncorrectOperationException {
        return null;  
    }

    @Override
    public PsiElement addBefore(@NotNull PsiElement element, PsiElement anchor) throws IncorrectOperationException {
        return null;  
    }

    @Override
    public PsiElement addAfter(@NotNull PsiElement element, PsiElement anchor) throws IncorrectOperationException {
        return null;  
    }

    @Override
    public void checkAdd(@NotNull PsiElement element) throws IncorrectOperationException {
        
    }

    @Override
    public PsiElement addRange(PsiElement first, PsiElement last) throws IncorrectOperationException {
        return null;  
    }

    @Override
    public PsiElement addRangeBefore(@NotNull PsiElement first, @NotNull PsiElement last, PsiElement anchor) throws IncorrectOperationException {
        return null;  
    }

    @Override
    public PsiElement addRangeAfter(PsiElement first, PsiElement last, PsiElement anchor) throws IncorrectOperationException {
        return null;  
    }

    @Override
    public void delete() throws IncorrectOperationException {
        
    }

    @Override
    public void checkDelete() throws IncorrectOperationException {
        
    }

    @Override
    public void deleteChildRange(PsiElement first, PsiElement last) throws IncorrectOperationException {
        
    }

    @Override
    public PsiElement replace(@NotNull PsiElement newElement) throws IncorrectOperationException {
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
    public ASTNode getNode() {
        return null;  
    }

    @Override
    public boolean isEquivalentTo(PsiElement another) {
        return false;  
    }

    @Override
    @NotNull
    public PsiDirectory[] getSubdirectories() {
        return new PsiDirectory[0];  
    }

    @Override
    @NotNull
    public PsiFile[] getFiles() {
        return new PsiFile[0];  
    }

    @Override
    public PsiDirectory findSubdirectory(@NotNull String name) {
        return null;  
    }

    @Override
    public PsiFile findFile(@NotNull String name) {
        return null;  
    }

    @Override
    @NotNull
    public PsiDirectory createSubdirectory(@NotNull String name) throws IncorrectOperationException {
        throw new IncorrectOperationException("Operation not supported");
    }

    @Override
    public void checkCreateSubdirectory(@NotNull String name) throws IncorrectOperationException {
        throw new IncorrectOperationException("Operation not supported");
    }

    @Override
    @NotNull
    public PsiFile createFile(@NotNull String name) throws IncorrectOperationException {
        throw new IncorrectOperationException("Operation not supported");
    }

    @Override
    @NotNull
    public PsiFile copyFileFrom(@NotNull String newName, @NotNull PsiFile originalFile) throws IncorrectOperationException {
        throw new IncorrectOperationException("Operation not supported");
    }

    @Override
    public void checkCreateFile(@NotNull String name) throws IncorrectOperationException {
        throw new IncorrectOperationException("Operation not supported");
    }

    @Override
    public void navigate(boolean requestFocus) {
        getConnection().getObjectBundle().navigate(requestFocus);
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
    public void checkSetName(String name) throws IncorrectOperationException {
        
    }

    @Override
    public Icon getIcon(int flags) {
        return getVirtualFile().getIcon();
    }

    @Override
    public <T> T getUserData(@NotNull Key<T> key) {
        return null;  
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {
        
    }
}
