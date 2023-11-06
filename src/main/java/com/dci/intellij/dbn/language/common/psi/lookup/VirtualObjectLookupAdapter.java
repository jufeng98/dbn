package com.dci.intellij.dbn.language.common.psi.lookup;

import com.dci.intellij.dbn.language.common.psi.BasePsiElement;
import com.dci.intellij.dbn.object.type.DBObjectType;

public class VirtualObjectLookupAdapter extends PsiLookupAdapter {
    private final DBObjectType parentObjectType;
    private final DBObjectType objectType;

    public VirtualObjectLookupAdapter(DBObjectType parentObjectType, DBObjectType objectType) {
        this.parentObjectType = parentObjectType;
        this.objectType = objectType;
    }

    @Override
    public boolean accepts(BasePsiElement element) {
        // TODO cleanup (nested DATASET structures skip drilling further into the element)
        //DBObjectType virtualObjectType = element.getElementType().getVirtualObjectType();
        //return parentObjectType == null || virtualObjectType == null || !parentObjectType.matches(virtualObjectType);
        return true;
    }

    @Override
    public boolean matches(BasePsiElement basePsiElement) {
        DBObjectType virtualObjectType = basePsiElement.getElementType().getVirtualObjectType();
        return virtualObjectType != null && virtualObjectType.matches(objectType);
    }

/*    private int getLevel(DBObjectType objectType) {
        switch (objectType) {
            case DATASET:
            case CURSOR:
            case TYPE: return 0;
            case COLUMN:
            case TYPE_ATTRIBUTE: return 1;
            default: throw new IllegalArgumentException("Level not defined for object type " + objectType);
        }
    }*/

}
