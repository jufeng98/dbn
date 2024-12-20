package com.dbn.mybatis.custom;

import com.intellij.lang.Language;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.ui.LanguageTextField;
import com.intellij.util.LocalTimeCounter;
import org.jetbrains.annotations.Nullable;

/**
 * @author yudong
 */
public class MyDocumentCreator implements LanguageTextField.DocumentCreator {

    @Override
    @SuppressWarnings("DataFlowIssue")
    public Document createDocument(String value, @Nullable Language language, Project project) {
        long stamp = LocalTimeCounter.currentTime();
        PsiFileFactory factory = PsiFileFactory.getInstance(project);

        LanguageFileType fileType = language.getAssociatedFileType();
        String defaultExtension = fileType.getDefaultExtension();

        PsiFile psiFile = ReadAction.compute(
                () -> factory.createFileFromText(CustomPluginHandler.PLUGIN_SIMPLE_NAME + "." + defaultExtension,
                        fileType, value, stamp, true, false)
        );

        return ReadAction.compute(() -> PsiDocumentManager.getInstance(project).getDocument(psiFile));
    }

}
