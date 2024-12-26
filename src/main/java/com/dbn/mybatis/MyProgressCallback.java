package com.dbn.mybatis;

import com.google.common.collect.Lists;
import com.intellij.codeInsight.actions.AbstractLayoutCodeProcessor;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.api.ProgressCallback;

import java.io.File;
import java.util.List;

@Slf4j
public class MyProgressCallback implements ProgressCallback {
    private final MyDefaultShellCallback shellCallback;
    @Getter
    private final List<File> files = Lists.newArrayList();

    public MyProgressCallback(MyDefaultShellCallback shellCallback) {
        this.shellCallback = shellCallback;
    }

    @Override
    public void startTask(String taskName) {
        log.warn("{}", taskName);
        if (!taskName.contains("Saving")) {
            return;
        }

        String fileName = taskName.replace("Saving file ", "");
        if (!fileName.endsWith("java")) {
            return;
        }

        String file = shellCallback.getDirectory() + "/" + fileName;

        files.add(new File(file));
    }

    public void reformatCode(List<VirtualFile> virtualFiles) {
        PsiFile[] psiFiles = virtualFiles.stream()
                .map(it -> PsiUtil.getPsiFile(shellCallback.getProject(), it))
                .toArray(PsiFile[]::new);

        AbstractLayoutCodeProcessor processor = new ReformatCodeProcessor(shellCallback.getProject(), psiFiles,
                null, false);
        // processor = new OptimizeImportsProcessor(processor);
        processor.run();
    }
}
