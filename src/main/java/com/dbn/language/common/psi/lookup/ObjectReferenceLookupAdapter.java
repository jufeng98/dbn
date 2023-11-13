package com.dbn.language.common.psi.lookup;

import com.dbn.language.common.element.util.ElementTypeAttribute;
import com.dbn.language.common.element.util.IdentifierCategory;
import com.dbn.language.common.psi.LeafPsiElement;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;

public class ObjectReferenceLookupAdapter extends ObjectLookupAdapter {

    public ObjectReferenceLookupAdapter(LeafPsiElement lookupIssuer, DBObjectType objectType, CharSequence identifierName) {
        super(lookupIssuer, IdentifierCategory.REFERENCE, objectType, identifierName);
    }

    public ObjectReferenceLookupAdapter(LeafPsiElement lookupIssuer, @NotNull DBObjectType objectType, CharSequence identifierName, ElementTypeAttribute attribute) {
        super(lookupIssuer, IdentifierCategory.REFERENCE, objectType, identifierName, attribute);
    }
}
