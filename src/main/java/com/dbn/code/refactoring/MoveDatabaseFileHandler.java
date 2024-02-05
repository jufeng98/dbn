package com.dbn.code.refactoring;

import com.dbn.language.common.DBLanguage;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.refactoring.move.MoveCallback;
import com.intellij.refactoring.move.MoveHandlerDelegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class MoveDatabaseFileHandler extends MoveHandlerDelegate {
    @Override
    public boolean canMove(PsiElement[] elements, @Nullable PsiElement targetContainer, @Nullable PsiReference reference) {
        return super.canMove(elements, targetContainer, reference);
    }

    @Override
    public boolean canMove(DataContext dataContext) {
        return super.canMove(dataContext);
    }

    @Override
    public boolean isValidTarget(@Nullable PsiElement targetElement, PsiElement[] sources) {
        return super.isValidTarget(targetElement, sources);
    }

    @Override
    public void doMove(Project project, PsiElement[] elements, @Nullable PsiElement targetContainer, @Nullable MoveCallback callback) {
        super.doMove(project, elements, targetContainer, callback);
    }

    @Override
    public PsiElement adjustTargetForMove(DataContext dataContext, PsiElement targetContainer) {
        return super.adjustTargetForMove(dataContext, targetContainer);
    }

    @Override
    public PsiElement @Nullable [] adjustForMove(Project project, PsiElement[] sourceElements, PsiElement targetElement) {
        return super.adjustForMove(project, sourceElements, targetElement);
    }

    @Override
    public boolean tryToMove(PsiElement element, Project project, DataContext dataContext, @Nullable PsiReference reference, Editor editor) {
        return super.tryToMove(element, project, dataContext, reference, editor);
    }

    @Override
    public void collectFilesOrDirsFromContext(DataContext dataContext, Set<PsiElement> filesOrDirs) {
        super.collectFilesOrDirsFromContext(dataContext, filesOrDirs);
    }

    @Override
    public boolean isMoveRedundant(PsiElement source, PsiElement target) {
        return super.isMoveRedundant(source, target);
    }

    @Override
    public @Nullable String getActionName(PsiElement @NotNull [] elements) {
        return super.getActionName(elements);
    }

    @Override
    public boolean supportsLanguage(@NotNull Language language) {
        return language instanceof DBLanguage;
    }
}
