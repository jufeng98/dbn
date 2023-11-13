package com.dbn.language.sql.structure;

import com.dbn.common.icon.Icons;
import com.dbn.language.common.element.util.ElementTypeAttribute;
import com.dbn.language.common.psi.BasePsiElement;
import com.dbn.language.common.psi.ChameleonPsiElement;
import com.dbn.language.common.psi.IdentifierPsiElement;
import com.dbn.language.common.psi.NamedPsiElement;
import com.dbn.language.psql.PSQLLanguage;
import com.dbn.object.type.DBObjectType;
import com.intellij.ide.util.treeView.smartTree.ActionPresentation;
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class SQLStructureViewModelFilter implements Filter {
    private final ActionPresentation actionPresentation = new ActionPresentationData("Top Level Elements", "", Icons.TOP_LEVEL_FILTER);

    @Override
    @NotNull
    public ActionPresentation getPresentation() {
        return actionPresentation;
    }

    @Override
    @NotNull
    public String getName() {
        return "Top Level";
    }

    @Override
    public boolean isVisible(TreeElement treeNode) {
        if (treeNode instanceof SQLStructureViewElement) {
            SQLStructureViewElement structureViewElement = (SQLStructureViewElement) treeNode;
            PsiElement psiElement = structureViewElement.getPsiElement();
            if (psiElement.getLanguage()== PSQLLanguage.INSTANCE) {
                if (psiElement instanceof ChameleonPsiElement) {
                    return true;
                }
                if (psiElement instanceof NamedPsiElement) {
                    NamedPsiElement namedPsiElement = (NamedPsiElement) psiElement;
                    boolean isObject = namedPsiElement.is(ElementTypeAttribute.OBJECT_DEFINITION) ||
                            namedPsiElement.is(ElementTypeAttribute.OBJECT_DECLARATION) ||
                            namedPsiElement.is(ElementTypeAttribute.OBJECT_SPECIFICATION);

                    if (isObject) {
                        BasePsiElement subject = namedPsiElement.findFirstPsiElement(ElementTypeAttribute.SUBJECT);
                        if (subject instanceof IdentifierPsiElement) {
                            IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) subject;
                            DBObjectType objectType = identifierPsiElement.getObjectType();
                            if (objectType.matches(DBObjectType.METHOD) || objectType.matches(DBObjectType.PROGRAM)) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isReverted() {
        return false;
    }
}
