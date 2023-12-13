package com.dbn.diagnostics.data;

import com.dbn.common.util.Lists;
import com.dbn.language.common.DBLanguagePsiFile;
import com.dbn.language.common.TokenType;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public final class ParserDiagnosticsUtil {

    @NotNull
    public static StateTransition computeStateTransition(
            @Nullable IssueCounter oldIssues,
            @Nullable IssueCounter newIssues) {

        int oldCount = oldIssues == null ? 0 : oldIssues.issueCount();
        int newCount = newIssues == null ? 0 : newIssues.issueCount();
        if (newCount == 0) {
            return StateTransition.FIXED;
        }

        if (oldCount == 0) {
            return StateTransition.BROKEN;
        }

        if (newCount > oldCount) {
            return StateTransition.DEGRADED;
        }

        if (newCount < oldCount) {
            return StateTransition.IMPROVED;
        }

        return StateTransition.UNCHANGED;
    }

    public static int countErrors(PsiFile file) {
        List<PsiErrorElement> errors = new ArrayList<>();
        PsiElementVisitor visitor = new PsiRecursiveElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof PsiErrorElement) {
                    if (Lists.noneMatch(errors, error -> error.getTextOffset() == element.getTextOffset())) {
                        errors.add((PsiErrorElement) element);
                    }

                }
                super.visitElement(element);

            }
        };;
        visitor.visitFile(file);
        return errors.size();
    }

    public static int countWarnings(PsiFile file) {
        AtomicInteger count = new AtomicInteger();
        PsiElementVisitor visitor = new PsiRecursiveElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof PsiWhiteSpace || element instanceof PsiComment) {
                    // ignore
                } else if (element instanceof LeafPsiElement && element.getParent() instanceof DBLanguagePsiFile) {
                    LeafPsiElement leafPsiElement = (LeafPsiElement) element;
                    IElementType elementType = leafPsiElement.getElementType();
                    if (elementType instanceof com.dbn.language.common.TokenType) {
                        com.dbn.language.common.TokenType tokenType = (TokenType) elementType;

                        if (!tokenType.isCharacter() && !tokenType.isChameleon()) {
                            count.incrementAndGet();
                        }
                    }

                } else{
                    super.visitElement(element);
                }
            }
        };;
        visitor.visitFile(file);
        return count.get();
    }
}
