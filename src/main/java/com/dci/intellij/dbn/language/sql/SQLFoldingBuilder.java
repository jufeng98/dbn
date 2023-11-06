package com.dci.intellij.dbn.language.sql;

import com.dci.intellij.dbn.common.consumer.SetCollector;
import com.dci.intellij.dbn.common.util.Naming;
import com.dci.intellij.dbn.language.common.DBLanguageFoldingBuilder;
import com.dci.intellij.dbn.language.common.psi.*;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.lang.folding.LanguageFolding;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class SQLFoldingBuilder extends DBLanguageFoldingBuilder {

    @Override
    protected void createFoldingDescriptors(@NotNull PsiElement psiElement, Document document, List<FoldingDescriptor> descriptors, int nestingIndex) {
        PsiElement child = psiElement.getFirstChild();
        while (child != null) {
            FoldingContext context = new FoldingContext(descriptors, document, nestingIndex);
            if (child instanceof PsiComment) {
                PsiComment psiComment = (PsiComment) child;
                createCommentFolding(context, psiComment);
            }
            else if (child instanceof ExecutablePsiElement) {
                ExecutablePsiElement executablePsiElement = (ExecutablePsiElement) child;
                TextRange textRange = executablePsiElement.getTextRange();
                if (textRange.getLength() > 10) {
                    ASTNode childNode = executablePsiElement.getNode();
                    FoldingDescriptor descriptor = new FoldingDescriptor(childNode, textRange);
                    context.addDescriptor(descriptor);
                    createFoldingDescriptors(executablePsiElement, document, descriptors, 1);
                }
            } else if (child instanceof ChameleonPsiElement) {
                ChameleonPsiElement chameleonPsiElement = (ChameleonPsiElement) child;
                FoldingDescriptor descriptor = new FoldingDescriptor(
                        chameleonPsiElement.node,
                        chameleonPsiElement.getTextRange());
                context.addDescriptor(descriptor);

                FoldingBuilder foldingBuilder = LanguageFolding.INSTANCE.forLanguage(chameleonPsiElement.getLanguage());
                FoldingDescriptor[] nestedDescriptors = foldingBuilder.buildFoldRegions(chameleonPsiElement.node, document);
                descriptors.addAll(Arrays.asList(nestedDescriptors));

            } else if (child instanceof TokenPsiElement) {
                TokenPsiElement tokenPsiElement = (TokenPsiElement) child;
                createLiteralFolding(context, tokenPsiElement);
            } else if (child instanceof BasePsiElement) {
                BasePsiElement basePsiElement = (BasePsiElement) child;
                createAttributeFolding(context, basePsiElement);

                if (context.nestingIndex < 2) {
                    createFoldingDescriptors(basePsiElement, document, descriptors, 1);
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
            BasePsiElement basePsiElement = (BasePsiElement) psiElement;
            SetCollector<IdentifierPsiElement> subjects = SetCollector.linked();

            basePsiElement.collectSubjectPsiElements(subjects);
            StringBuilder buffer = new StringBuilder(" ");
            buffer.append(basePsiElement.getSpecificElementType().getDescription());
            if (subjects.isNotEmpty()) {
                buffer.append(" (");
                buffer.append(Naming.createNamesList(subjects.elements(), 3));
                buffer.append(")");
            }
            return buffer.toString();
        }
        return "...";
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false;
    }

}
