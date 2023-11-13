package com.dbn.language.common.psi.lookup;

import com.dbn.language.common.element.util.ElementTypeAttribute;
import com.dbn.language.common.element.util.IdentifierCategory;
import com.dbn.language.common.element.util.IdentifierType;
import com.dbn.language.common.psi.IdentifierPsiElement;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;

public class VariableReferenceLookupAdapter extends IdentifierLookupAdapter {
    public VariableReferenceLookupAdapter(IdentifierPsiElement lookupIssuer, DBObjectType objectType, CharSequence identifierName) {
        super(lookupIssuer, IdentifierType.VARIABLE, IdentifierCategory.REFERENCE, objectType, identifierName);
    }

    public VariableReferenceLookupAdapter(IdentifierPsiElement lookupIssuer, @NotNull DBObjectType objectType, CharSequence identifierName, ElementTypeAttribute attribute) {
        super(lookupIssuer, IdentifierType.VARIABLE, IdentifierCategory.REFERENCE, objectType, identifierName, attribute);
    }
}
