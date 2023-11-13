package com.dbn.language.common.psi.lookup;

import com.dbn.language.common.element.util.ElementTypeAttribute;
import com.dbn.language.common.element.util.IdentifierCategory;
import com.dbn.language.common.element.util.IdentifierType;
import com.dbn.language.common.psi.LeafPsiElement;
import com.dbn.object.type.DBObjectType;

public class SimpleObjectLookupAdapter extends IdentifierLookupAdapter {
    public SimpleObjectLookupAdapter(LeafPsiElement lookupIssuer, DBObjectType objectType) {
        super(lookupIssuer, IdentifierType.OBJECT, IdentifierCategory.ALL, objectType, null);
    }

    public SimpleObjectLookupAdapter(LeafPsiElement lookupIssuer, DBObjectType objectType, CharSequence identifierName) {
        super(lookupIssuer, IdentifierType.OBJECT, IdentifierCategory.ALL, objectType, identifierName);
    }

    public SimpleObjectLookupAdapter(LeafPsiElement lookupIssuer, IdentifierCategory identifierCategory, DBObjectType objectType) {
        super(lookupIssuer, IdentifierType.OBJECT, identifierCategory, objectType, null);
    }

    public SimpleObjectLookupAdapter(LeafPsiElement lookupIssuer, IdentifierCategory identifierCategory, DBObjectType objectType, CharSequence identifierName) {
        super(lookupIssuer, IdentifierType.OBJECT, identifierCategory, objectType, identifierName);
    }

    public SimpleObjectLookupAdapter(LeafPsiElement lookupIssuer, IdentifierCategory identifierCategory, DBObjectType objectType, CharSequence identifierName, ElementTypeAttribute attribute) {
        super(lookupIssuer, IdentifierType.OBJECT, identifierCategory, objectType, identifierName, attribute);
    }
}
