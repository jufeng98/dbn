package com.dbn.language.sql;

import com.dbn.language.common.psi.BasePsiElement;
import com.dbn.language.common.psi.IdentifierPsiElement;
import com.dbn.language.common.psi.PsiUtil;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.DBObjectPsiElement;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SQLDocumentationProvider implements DocumentationProvider {

    @Nullable
    private String getQuickNavigateInfo(PsiElement element) {
        if (element instanceof DBObjectPsiElement) {
            DBObjectPsiElement objectPsiElement = (DBObjectPsiElement) element;
            return objectPsiElement.ensureObject().getNavigationTooltipText();
        } else if (element instanceof IdentifierPsiElement) {
            IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) element;
             if (identifierPsiElement.isAlias()) {
                if (identifierPsiElement.isDefinition()) {
                    BasePsiElement aliasedObjectElement = PsiUtil.resolveAliasedEntityElement(identifierPsiElement);
                    if (aliasedObjectElement == null) {
                        return "unknown alias";
                    } else {
                        DBObject aliasedObject = aliasedObjectElement.getUnderlyingObject();
                        if (aliasedObject == null) {
                            return "alias of " + aliasedObjectElement.getReferenceQualifiedName();
                        } else {
                            return "alias of " + aliasedObject.getQualifiedNameWithType();
                        }
                    }
                }
            } 
        }
        return null;
    }

    @Override
    public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        return getQuickNavigateInfo(element);
    }

    @Override
    @Nullable
    public List<String> getUrlFor(PsiElement psiElement, PsiElement psiElement1) {
        return null;
    }

    @Override
    @Nullable
    public String generateDoc(PsiElement psiElement, PsiElement psiElement1) {
        return null;
    }

    @Override
    @Nullable
    public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object o, PsiElement psiElement) {
        return null;
    }

    @Override
    @Nullable
    public PsiElement getDocumentationElementForLink(PsiManager psiManager, String s, PsiElement psiElement) {
        return null;
    }
}
