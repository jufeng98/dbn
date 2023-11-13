package com.dbn.language.common.psi.lookup;

import com.dbn.language.common.element.util.ElementTypeAttribute;
import com.dbn.language.common.element.util.IdentifierCategory;
import com.dbn.language.common.psi.LeafPsiElement;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;

public class ObjectDefinitionLookupAdapter extends ObjectLookupAdapter {

    public ObjectDefinitionLookupAdapter(LeafPsiElement lookupIssuer, DBObjectType objectType, CharSequence identifierName) {
        super(lookupIssuer, IdentifierCategory.DEFINITION, objectType, identifierName);
    }

    public ObjectDefinitionLookupAdapter(LeafPsiElement lookupIssuer, @NotNull DBObjectType objectType, CharSequence identifierName, ElementTypeAttribute attribute) {
        super(lookupIssuer, IdentifierCategory.DEFINITION, objectType, identifierName, attribute);
    }
}
