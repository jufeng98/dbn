package com.dbn.object.common;

import com.dbn.common.dispose.Checks;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.SchemaId;
import com.dbn.connection.context.DatabaseContextBase;
import com.dbn.language.common.psi.EmptySearchScope;
import com.dbn.language.sql.SQLLanguage;
import com.dbn.navigation.ReadonlyPsiStub;
import com.dbn.object.lookup.DBObjectRef;
import com.dbn.object.type.DBObjectType;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.EverythingGlobalScope;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DBObjectPsiElement implements PsiNamedElement, ReadonlyPsiStub, NavigationItem, DatabaseContextBase {
    private final DBObjectRef<?> object;

    public DBObjectPsiElement(DBObjectRef<?> object) {
        this.object = object;
    }

    @Nullable
    @Override
    public String getName() {
        return object.getObjectName();
    }

    @Nullable
    @Override
    public ConnectionHandler getConnection() {
        return object.getConnection();
    }

    @Nullable
    @Override
    public SchemaId getSchemaId() {
        return object.getSchemaId();
    }

    @Nullable
    @Override
    public ItemPresentation getPresentation() {
        return ensureObject().getPresentation();
    }

    /*********************************************************
     *                    PsiNamedElement                    *
     *********************************************************/

    @Override
    public PsiManager getManager() {return PsiManager.getInstance(getProject());}

    @Override
    @NotNull
    public PsiElement[] getChildren() {
        return PsiElement.EMPTY_ARRAY;
    }

    @Override
    public PsiElement getParent(){return null;}

    @Override
    public PsiElement getFirstChild() {return null;}

    @Override
    public PsiElement getLastChild() {return null;}

    @Override
    public PsiElement getNextSibling() {return null;}

    @Override
    public PsiElement getPrevSibling() {return null;}

    @Override
    public PsiElement findElementAt(int offset) {return null;}

    @Override
    public PsiReference findReferenceAt(int offset) {return null;}

    @Override
    public PsiFile getContainingFile() throws PsiInvalidElementAccessException {
        return ensureObject().getObjectBundle().getFakeObjectFile();
    }

    @Override
    public PsiElement getOriginalElement() {return this;}

    @Override
    public boolean textMatches(@NotNull CharSequence text) {return false;}

    @Override
    public boolean textMatches(@NotNull PsiElement element) {return false;}

    @Override
    public boolean textContains(char c) {return false;}

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {}

    @Override
    public PsiElement getNavigationElement() {return this;}

    @Override
    public int getStartOffsetInParent() {return 0;}

    @Override
    public int getTextOffset() {return 0;}

    @Override
    public void acceptChildren(@NotNull PsiElementVisitor visitor) {}

    @Override
    public PsiElement copy() {return this;}

    @Override
    public boolean isValid() {
        DBObject object = getObject();
        return Checks.isValid(object) && Checks.isValid(object.getParentObject());
    }

    @Override
    public boolean isWritable() {return false;}

    @Override
    public PsiReference getReference() {return null;}

    @Override
    @NotNull
    public PsiReference[] getReferences() {return new PsiReference[0];}

    @Override
    public PsiElement getContext() {return null;}

    @Override
    public boolean isPhysical() {return false;}

    @Override
    @NotNull
    public GlobalSearchScope getResolveScope() {return EmptySearchScope.INSTANCE;}

    @Override
    @NotNull
    public SearchScope getUseScope() {return new EverythingGlobalScope();}

    @Override
    public ASTNode getNode() {return null;}

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor psiScopeProcessor, @NotNull ResolveState resolveState, @Nullable PsiElement psiElement, @NotNull PsiElement psiElement1) {return false;}

    @Override
    public <T> T getCopyableUserData(@NotNull Key<T> key) {return null;}

    @Override
    public <T> void putCopyableUserData(@NotNull Key<T> key, T value) {}

    @Override
    public <T> T getUserData(@NotNull Key<T> key) {return null;}

    @Override
    public <T> void putUserData(@NotNull Key<T> key, T value) {}

    @Override
    public boolean isEquivalentTo(PsiElement psiElement) {return false;}

    @NotNull
    @Override
    public Project getProject() throws PsiInvalidElementAccessException {
        return ensureObject().getProject();
    }

    @Override
    @NotNull
    public Language getLanguage() {
        return SQLLanguage.INSTANCE;
    }

    @Override
    public TextRange getTextRange() {
        return new TextRange(0, getText().length());
    }

    @Override
    public int getTextLength() {
        return getText().length();
    }

    @Override
    @NonNls
    public String getText() {
        return getName();
    }

    @Override
    @NotNull
    public char[] textToCharArray() {
        return getText().toCharArray();
    }





    @Override
    public void navigate(boolean requestFocus) {
        ensureObject().navigate(requestFocus);
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
    public Icon getIcon(int flags) {
        return ensureObject().getIcon();
    }

    @NotNull
    public DBObject ensureObject() {
        return DBObjectRef.ensure(object);
    }

    @Nullable
    public DBObject getObject() {
        return DBObjectRef.get(object);
    }

    @NotNull
    public DBObjectType getObjectType() {
        return object.getObjectType();
    }
}
