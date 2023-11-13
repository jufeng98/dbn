package com.dbn.language.common.resolve;

import com.dbn.language.common.psi.BasePsiElement;
import com.dbn.language.common.psi.IdentifierPsiElement;
import com.dbn.language.common.psi.NamedPsiElement;
import com.dbn.language.common.psi.lookup.ObjectReferenceLookupAdapter;
import com.dbn.language.common.psi.lookup.PsiLookupAdapter;
import com.dbn.object.common.DBObject;
import com.dbn.object.type.DBObjectType;

public class AssignmentObjectResolver extends UnderlyingObjectResolver{
    private static final AssignmentObjectResolver INSTANCE = new AssignmentObjectResolver();

    public static AssignmentObjectResolver getInstance() {
        return INSTANCE;
    }

    private AssignmentObjectResolver() {
        super("ASSIGNMENT_RESOLVER");
    }

    @Override
    protected DBObject resolve(IdentifierPsiElement identifierPsiElement, int recursionCheck) {
        NamedPsiElement enclosingNamedPsiElement = identifierPsiElement.findEnclosingNamedElement();
        if (enclosingNamedPsiElement == null) return null;

        PsiLookupAdapter lookupAdapter = new ObjectReferenceLookupAdapter(identifierPsiElement, DBObjectType.TYPE, null);
        BasePsiElement underlyingObjectCandidate = lookupAdapter.findInElement(enclosingNamedPsiElement);

        return underlyingObjectCandidate == null ? null : underlyingObjectCandidate.getUnderlyingObject() ;
    }
}
