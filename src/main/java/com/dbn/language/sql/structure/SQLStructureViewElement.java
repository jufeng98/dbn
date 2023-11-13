package com.dbn.language.sql.structure;

import com.dbn.language.common.DBLanguagePsiFile;
import com.dbn.language.common.psi.BasePsiElement;
import com.dbn.language.common.psi.ChameleonPsiElement;
import com.dbn.language.common.structure.DBLanguageStructureViewElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SQLStructureViewElement extends DBLanguageStructureViewElement<SQLStructureViewElement> {

    SQLStructureViewElement(PsiElement psiElement) {
        super(psiElement);
    }

    @Override
    @NotNull
    public ItemPresentation getPresentation() {
        PsiElement psiElement = getPsiElement();
        if (psiElement instanceof BasePsiElement) return (ItemPresentation) psiElement;
        return new ItemPresentation() {
            @Override
            public String getPresentableText() {
                if (psiElement instanceof DBLanguagePsiFile) {
                    DBLanguagePsiFile file = (DBLanguagePsiFile) psiElement;
                    return file.getName();
                }
                if (psiElement instanceof ChameleonPsiElement) {
                    ChameleonPsiElement chameleonPsiElement = (ChameleonPsiElement) psiElement;
                    //return chameleonPsiElement.getLanguage().getName() + " block";
                    // todo make this dynamic
                    return "PL/SQL block";
                }
                return psiElement.getText();
            }

            @Override
            @Nullable
            public String getLocationString() {
                return null;
            }

            @Override
            @Nullable
            public Icon getIcon(boolean open) {
                return psiElement.isValid() ? psiElement.getIcon(Iconable.ICON_FLAG_VISIBILITY) : null;
            }

            @Nullable
            public TextAttributesKey getTextAttributesKey() {
                return null;
            }
        };
    }

    @Override
    protected SQLStructureViewElement createChildElement(PsiElement child) {
        return new SQLStructureViewElement(child);
    }

    @Override
    protected List<SQLStructureViewElement> visitChild(PsiElement child, List<SQLStructureViewElement> elements) {
        if (child instanceof ChameleonPsiElement) {
            if (elements == null) {
                elements = new ArrayList<>();
            }
            elements.add(new SQLStructureViewElement(child));
            return elements;
        } else {
            return super.visitChild(child, elements);
        }
    }

}
