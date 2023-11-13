package com.dbn.language.common.structure;

import com.dbn.language.common.DBLanguagePsiFile;
import com.dbn.language.common.element.util.ElementTypeAttribute;
import com.dbn.language.common.psi.BasePsiElement;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.PsiElement;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class DBLanguageStructureViewElement<T> implements StructureViewTreeElement {
    private final PsiElement psiElement;

    public DBLanguageStructureViewElement(PsiElement psiElement) {
        this.psiElement = psiElement;
    }

    @Override
    public Object getValue() {
        return psiElement;
    }

    @Override
    @NotNull
    public ItemPresentation getPresentation() {
        if (psiElement instanceof BasePsiElement) return (ItemPresentation) psiElement;
        return new ItemPresentation() {
            @Override
            public String getPresentableText() {
                if (psiElement instanceof DBLanguagePsiFile) {
                    DBLanguagePsiFile file = (DBLanguagePsiFile) psiElement;
                    return file.getName();
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
    @NotNull
    public StructureViewTreeElement[] getChildren() {
        List<T> elements = getChildren(psiElement, null);
        return elements == null ?
                EMPTY_ARRAY :
                elements.toArray(new StructureViewTreeElement[0]);
    }

    private List<T> getChildren(PsiElement parent, List<T> elements) {
        PsiElement child = parent.getFirstChild();
        while (child != null) {
            elements = visitChild(child, elements);
            child = child.getNextSibling();
        }
        return elements;
    }

    protected List<T> visitChild(PsiElement child, List<T> elements) {
        if (child instanceof BasePsiElement) {
            BasePsiElement basePsiElement = (BasePsiElement) child;
            if (basePsiElement.is(ElementTypeAttribute.STRUCTURE)) {
                if (elements == null) {
                    elements = new ArrayList<>();
                }
                elements.add(createChildElement(child));
            } else {
                elements = getChildren(basePsiElement, elements);
            }
        }
        return elements;
    }

    protected abstract T createChildElement(PsiElement child);

    @Override
    public void navigate(boolean requestFocus) {
        if (psiElement instanceof NavigationItem) {
            NavigationItem navigationItem = (NavigationItem) psiElement;
            navigationItem.navigate(requestFocus);
        }
    }

    @Override
    public boolean canNavigate() {
        return true;
    }

    @Override
    public boolean canNavigateToSource() {
        return true;
    }
}
