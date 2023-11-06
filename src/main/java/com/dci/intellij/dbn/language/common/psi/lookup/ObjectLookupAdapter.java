package com.dci.intellij.dbn.language.common.psi.lookup;

import com.dci.intellij.dbn.language.common.element.util.ElementTypeAttribute;
import com.dci.intellij.dbn.language.common.element.util.IdentifierCategory;
import com.dci.intellij.dbn.language.common.element.util.IdentifierType;
import com.dci.intellij.dbn.language.common.psi.BasePsiElement;
import com.dci.intellij.dbn.language.common.psi.LeafPsiElement;
import com.dci.intellij.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ObjectLookupAdapter extends IdentifierLookupAdapter {
    public ObjectLookupAdapter(@Nullable LeafPsiElement lookupIssuer, DBObjectType objectType) {
        super(lookupIssuer, IdentifierType.OBJECT, IdentifierCategory.ALL, objectType, null);
    }

    public ObjectLookupAdapter(@Nullable LeafPsiElement lookupIssuer, DBObjectType objectType, CharSequence identifierName) {
        super(lookupIssuer, IdentifierType.OBJECT, IdentifierCategory.ALL, objectType, identifierName);
    }

    public ObjectLookupAdapter(@Nullable LeafPsiElement lookupIssuer, IdentifierCategory identifierCategory, DBObjectType objectType) {
        super(lookupIssuer, IdentifierType.OBJECT, identifierCategory, objectType, null);
    }

    public ObjectLookupAdapter(@Nullable LeafPsiElement lookupIssuer, IdentifierCategory identifierCategory, DBObjectType objectType, CharSequence identifierName) {
        super(lookupIssuer, IdentifierType.OBJECT, identifierCategory, objectType, identifierName);
    }

    public ObjectLookupAdapter(@Nullable LeafPsiElement lookupIssuer, @Nullable IdentifierCategory identifierCategory, @NotNull DBObjectType objectType, CharSequence identifierName, ElementTypeAttribute attribute) {
        super(lookupIssuer, IdentifierType.OBJECT, identifierCategory, objectType, identifierName, attribute);
    }

    @Override
    public boolean matches(BasePsiElement basePsiElement) {
        if (super.matches(basePsiElement)) return true;

        DBObjectType virtualObjectType = basePsiElement.getElementType().getVirtualObjectType();
        return virtualObjectType != null && virtualObjectType.matches(getObjectType());
    }
}
