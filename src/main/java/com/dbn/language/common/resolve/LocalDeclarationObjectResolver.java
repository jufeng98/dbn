package com.dbn.language.common.resolve;

import com.dbn.language.common.psi.BasePsiElement;
import com.dbn.language.common.psi.IdentifierPsiElement;
import com.dbn.language.common.psi.NamedPsiElement;
import com.dbn.language.common.psi.lookup.IdentifierLookupAdapter;
import com.dbn.language.common.psi.lookup.PsiLookupAdapter;
import com.dbn.object.common.DBObject;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;

public class LocalDeclarationObjectResolver extends UnderlyingObjectResolver{
    private static final LocalDeclarationObjectResolver INSTANCE = new LocalDeclarationObjectResolver();

    public static LocalDeclarationObjectResolver getInstance() {
        return INSTANCE;
    }

    private LocalDeclarationObjectResolver() {
        super("LOCAL_DECLARATION_RESOLVER");
    }

    @Override
    protected DBObject resolve(IdentifierPsiElement identifierPsiElement, int recursionCheck) {

        NamedPsiElement enclosingNamedPsiElement = identifierPsiElement.findEnclosingNamedElement();
        if (enclosingNamedPsiElement == null) return null;

        BasePsiElement underlyingObjectCandidate;
        DBObjectType objectType = identifierPsiElement.getObjectType();
        if (objectType.matches(DBObjectType.DATASET)) {
            underlyingObjectCandidate = findObject(identifierPsiElement, enclosingNamedPsiElement, DBObjectType.DATASET);

        } else if (objectType.matches(DBObjectType.TYPE)) {
            underlyingObjectCandidate = findObject(identifierPsiElement, enclosingNamedPsiElement, DBObjectType.TYPE);

        } else if (objectType == DBObjectType.ANY || objectType == DBObjectType.ARGUMENT) {
            underlyingObjectCandidate = findObject(identifierPsiElement, enclosingNamedPsiElement, DBObjectType.TYPE);
            if (underlyingObjectCandidate == null) {
                underlyingObjectCandidate = findObject(identifierPsiElement, enclosingNamedPsiElement, DBObjectType.DATASET);
            }
        } else {
            underlyingObjectCandidate = findObject(identifierPsiElement, enclosingNamedPsiElement, objectType);
        }

        return underlyingObjectCandidate == null ? null : underlyingObjectCandidate.getUnderlyingObject() ;
    }

    private static BasePsiElement findObject(IdentifierPsiElement identifierPsiElement, @NotNull NamedPsiElement enclosingNamedPsiElement, DBObjectType objectType) {
        PsiLookupAdapter lookupAdapter = new IdentifierLookupAdapter(identifierPsiElement, null, null, objectType, null);
        return lookupAdapter.findInElement(enclosingNamedPsiElement);
    }
}
