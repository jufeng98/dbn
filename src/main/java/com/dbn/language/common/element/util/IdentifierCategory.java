package com.dbn.language.common.element.util;

import com.dbn.language.common.psi.IdentifierPsiElement;
import com.dbn.common.util.Enumerations;

public enum IdentifierCategory {
    DEFINITION,
    REFERENCE,
    UNKNOWN,
    ALL;

    public boolean matches(IdentifierPsiElement identifierPsiElement) {
        switch (this) {
            case DEFINITION: return identifierPsiElement.isDefinition();
            case REFERENCE: return identifierPsiElement.isReference();
            default: return true;
        }
    }

    public boolean isOneOf(IdentifierCategory... categories) {
        return Enumerations.isOneOf(this, categories);
    }
}
