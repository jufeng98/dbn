package com.dci.intellij.dbn.object.common;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.common.metadata.DBObjectMetadata;
import com.dci.intellij.dbn.object.type.DBObjectType;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class DBFictiveObject extends DBObjectImpl implements PsiReference {
    private String name;
    public DBFictiveObject(DBObjectType objectType, String name) {
        super(null, objectType, name);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isVirtual() {
        return true;
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBObjectMetadata metadata) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getQualifiedNameWithType() {
        return getName();
    }

    @NotNull
    @Override
    public DBObjectType getObjectType() {
        return ref().getObjectType();
    }


    @Override
    public void navigate(boolean requestFocus) {

    }

    /*********************************************************
     *                       PsiReference                    *
     *********************************************************/
    @Override
    @NotNull
    public PsiElement getElement() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public TextRange getRangeInElement() {
        return new TextRange(0, name.length());
    }

    @Override
    public PsiElement resolve() {
        return null;
    }

    @Override
    @NotNull
    public String getCanonicalText() {
        return name;
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
        return false;
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

}
