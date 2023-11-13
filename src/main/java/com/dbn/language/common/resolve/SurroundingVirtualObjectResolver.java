package com.dbn.language.common.resolve;

import com.dbn.language.common.psi.BasePsiElement;
import com.dbn.language.common.psi.IdentifierPsiElement;
import com.dbn.object.common.DBObject;
import com.dbn.object.type.DBObjectType;

public class SurroundingVirtualObjectResolver extends UnderlyingObjectResolver{
    private static final SurroundingVirtualObjectResolver INSTANCE = new SurroundingVirtualObjectResolver();

    public static SurroundingVirtualObjectResolver getInstance() {
        return INSTANCE;
    }

    private SurroundingVirtualObjectResolver() {
        super("VIRTUAL_OBJECT_RESOLVER");
    }

    @Override
    protected DBObject resolve(IdentifierPsiElement identifierPsiElement, int recursionCheck) {
        DBObjectType objectType = identifierPsiElement.getObjectType();
        if (objectType == DBObjectType.DATASET) return null;

        BasePsiElement virtualObjectPsiElement = identifierPsiElement.findEnclosingVirtualObjectElement(objectType);
        if (virtualObjectPsiElement == null) return null;

        return virtualObjectPsiElement.getUnderlyingObject();

    }
}
