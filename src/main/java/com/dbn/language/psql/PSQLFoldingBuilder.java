package com.dbn.language.psql;

import com.dbn.language.common.DBLanguageFoldingBuilder;
import com.dbn.language.common.element.util.ElementTypeAttribute;
import com.dbn.language.common.psi.BasePsiElement;
import com.dbn.language.common.psi.ChameleonPsiElement;
import com.dbn.language.common.psi.TokenPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PSQLFoldingBuilder extends DBLanguageFoldingBuilder {

    @Override
    protected void createFoldingDescriptors(@NotNull PsiElement psiElement, Document document, List<FoldingDescriptor> descriptors, int nestingIndex) {
        PsiElement child = psiElement.getFirstChild();
        while (child != null) {
            FoldingContext context = new FoldingContext(descriptors, document, nestingIndex);
            if (child instanceof PsiComment) {
                createCommentFolding(context, (PsiComment) child);
            }
            else if (child instanceof BasePsiElement) {
                BasePsiElement basePsiElement = (BasePsiElement) child;
                createAttributeFolding(context, basePsiElement);

                if (!context.folded && basePsiElement.is(ElementTypeAttribute.STATEMENT)) {
                    if (basePsiElement.containsLineBreaks()) {
                        TextRange textRange = null;

                        BasePsiElement firstPsiElement = basePsiElement.findFirstLeafPsiElement();
                        int firstElementEndOffset = firstPsiElement.getTextOffset() + firstPsiElement.getTextLength();
                        int firstElementLineNumber = document.getLineNumber(firstElementEndOffset);


                        BasePsiElement subjectPsiElement = basePsiElement.findFirstPsiElement(ElementTypeAttribute.SUBJECT);
                        int blockEndOffset = basePsiElement.getTextOffset() + basePsiElement.getTextLength();
                        if (subjectPsiElement != null && subjectPsiElement.getParent() == basePsiElement) {
                            int subjectEndOffset = subjectPsiElement.getTextOffset() + subjectPsiElement.getTextLength();
                            int subjectLineNumber = document.getLineNumber(subjectEndOffset);

                            if (subjectLineNumber == firstElementLineNumber) {
                                textRange = new TextRange(subjectEndOffset, blockEndOffset);
                            }
                        }

                        if (textRange == null) {
                            textRange = new TextRange(firstElementEndOffset, blockEndOffset);
                        }

                        if (textRange.getLength() > 10) {
                            FoldingDescriptor foldingDescriptor = new FoldingDescriptor(basePsiElement.getNode(), textRange);
                            context.addDescriptor(foldingDescriptor);
                        }
                    }
                }

                if (!context.folded && child instanceof TokenPsiElement) {
                    TokenPsiElement tokenPsiElement = (TokenPsiElement) child;
                    createLiteralFolding(context, tokenPsiElement);
                }


                if (context.nestingIndex < 9) {
                    createFoldingDescriptors(child, document, descriptors, context.nestingIndex);
                }
            }
            child = child.getNextSibling();
        }
    }

    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        PsiElement psiElement = node.getPsi();
        if (psiElement instanceof PsiComment) {
            return "/*...*/";
        }

        if (psiElement instanceof BasePsiElement) {
            /*BasePsiElement basePsiElement = (BasePsiElement) psiElement;
            PsiElement subject = basePsiElement.lookupFirstSubjectPsiElement();
            StringBuilder buffer = new StringBuilder(basePsiElement.getElementType().getDescription());
            if (subject != null) {
                buffer.append(" (");
                buffer.append(subject.getText());
                buffer.append(")");
            }
            return buffer.toString();*/
            return "...";
        }

        if (psiElement instanceof ChameleonPsiElement) {
            ChameleonPsiElement chameleonPsiElement = (ChameleonPsiElement) psiElement;
            return chameleonPsiElement.getLanguage().getDisplayName() + " block";
        }
        return "";
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false;
    }

}
