package com.dbn.language.psql.structure;

import com.dbn.language.common.structure.DBLanguageStructureViewElement;
import com.intellij.psi.PsiElement;

public class PSQLStructureViewElement extends DBLanguageStructureViewElement<PSQLStructureViewElement> {

    PSQLStructureViewElement(PsiElement psiElement) {
        super(psiElement);
    }

    @Override
    protected PSQLStructureViewElement createChildElement(PsiElement child) {
        return new PSQLStructureViewElement(child);
    }

}
