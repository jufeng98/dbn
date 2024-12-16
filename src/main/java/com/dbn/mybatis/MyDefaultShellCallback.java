package com.dbn.mybatis;

import com.dbn.utils.VirtualFileUtils;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MyDefaultShellCallback extends DefaultShellCallback {
    private final Project project;

    public MyDefaultShellCallback(boolean overwrite, @NotNull Project project) {
        super(overwrite);
        this.project = project;
    }

    @Override
    public String mergeJavaFile(String newFileSource, File existingFile, String[] javadocTags, String fileEncoding) {
        final String[] res = new String[1];
        ApplicationManager.getApplication().invokeAndWait(() ->
                res[0] = WriteAction.computeAndWait(() -> {
                    VirtualFile virtualFile = VirtualFileUtils.createVirtualFileFromText(newFileSource.getBytes(StandardCharsets.UTF_8),
                            existingFile.getName());

                    PsiFile psiFile = PsiUtil.getPsiFile(project, virtualFile);

                    ReformatCodeProcessor processor = new ReformatCodeProcessor(psiFile, false);
                    processor.setProcessAllFilesAsSingleUndoStep(false);
                    processor.run();

                    try {
                        byte[] bytes = virtualFile.contentsToByteArray();
                        return new String(bytes, StandardCharsets.UTF_8);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }));

        return res[0];
    }

    @Override
    public boolean isMergeSupported() {
        return true;
    }
}
