package com.dci.intellij.dbn.language.common.psi;

import com.dci.intellij.dbn.common.consumer.SetCollector;
import com.dci.intellij.dbn.common.util.Naming;
import com.dci.intellij.dbn.language.common.element.impl.NamedElementType;
import com.dci.intellij.dbn.language.common.element.util.ElementTypeAttribute;
import com.dci.intellij.dbn.language.common.psi.lookup.PsiLookupAdapter;
import com.dci.intellij.dbn.vfs.file.DBSourceCodeVirtualFile;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class NamedPsiElement extends SequencePsiElement<NamedElementType> {
    public NamedPsiElement(ASTNode astNode, NamedElementType elementType) {
        super(astNode, elementType);
    }

    @Nullable
    public String createSubjectList() {
        SetCollector<IdentifierPsiElement> subjects = SetCollector.linked();
        collectSubjectPsiElements(subjects);
        return subjects.isNotEmpty() ? Naming.createNamesList(subjects.elements(), 3) : null;
    }

    @Override
    public boolean hasErrors() {
        PsiElement child = getFirstChild();
        while (child != null) {
            if (child instanceof BasePsiElement && !(child instanceof NamedPsiElement)) {
                BasePsiElement basePsiElement = (BasePsiElement) child;
                if (basePsiElement.hasErrors()) {
                    return true;
                }
            }
            child = child.getNextSibling();
        }
        return false;
    }

    /*********************************************************
     *                       ItemPresentation                *
     *********************************************************/
    @Override
    public String getPresentableText() {
        BasePsiElement subject = findFirstPsiElement(ElementTypeAttribute.SUBJECT);
        if (subject instanceof IdentifierPsiElement && subject.getParent() == this) {
            IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) subject;
            if (identifierPsiElement.isObject()) {
                return identifierPsiElement.getText();
            }
        }
        return super.getPresentableText();
    }

    @Override
    @Nullable
    public String getLocationString() {
        BasePsiElement subject = findFirstPsiElement(ElementTypeAttribute.SUBJECT);
        if (subject instanceof IdentifierPsiElement && subject.getParent() == this) {

        } else {
            if (is(ElementTypeAttribute.STRUCTURE)) {
                if (subject != null) {
                    return subject.getText();
                }
            }
        }
        return null;
    }

    @Override
    @Nullable
    public Icon getIcon(boolean open) {
        Icon icon = super.getIcon(open);
        if (icon != null) return icon;

        BasePsiElement subject = findFirstPsiElement(ElementTypeAttribute.SUBJECT);
        if (subject == null) return null;
        if (subject.getParent() != this) return null;

        if (subject instanceof IdentifierPsiElement) {
            IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) subject;
            if (!identifierPsiElement.isObject()) return null;
            if (!identifierPsiElement.isValid()) return null;

            VirtualFile file = PsiUtil.getVirtualFileForElement(identifierPsiElement);
            if (file instanceof DBSourceCodeVirtualFile) {
                DBSourceCodeVirtualFile sourceCodeFile = (DBSourceCodeVirtualFile) file;
                return identifierPsiElement.getObjectType().getIcon(sourceCodeFile.getContentType());
            }
            return identifierPsiElement.getObjectType().getIcon();
        }
        return null;
    }

    @Nullable
    @Override
    public BasePsiElement findPsiElement(PsiLookupAdapter lookupAdapter, int scopeCrossCount) {
        ProgressManager.checkCanceled();
        return super.findPsiElement(lookupAdapter, scopeCrossCount);
    }

    @Override
    @Nullable
    public TextAttributesKey getTextAttributesKey() {
        return null;
    }
}
