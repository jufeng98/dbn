package com.dbn.vfs;

import com.dbn.common.util.Commons;
import com.dbn.language.common.DBLanguageFileType;
import com.dbn.vfs.file.DBObjectVirtualFile;
import com.dbn.vfs.file.DBSourceCodeVirtualFile;
import com.intellij.lang.Language;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.FileViewProviderFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.SingleRootFileViewProvider;
import com.intellij.testFramework.LightVirtualFile;
import org.jetbrains.annotations.NotNull;

public class DatabaseFileViewProviderFactory implements FileViewProviderFactory{

    @Override
    @NotNull
    public FileViewProvider createFileViewProvider(@NotNull VirtualFile file, Language language, @NotNull PsiManager manager, boolean eventSystemEnabled) {

        if (file instanceof DBObjectVirtualFile ||
                file instanceof DBSourceCodeVirtualFile ||
                ((file instanceof DBVirtualFile || file instanceof LightVirtualFile) && file.getFileType() instanceof DBLanguageFileType)) {

            if (file instanceof DBVirtualFile virtualFile) {

                return Commons.nvl(virtualFile.getCachedViewProvider(),
                        () -> createViewProvider(
                                file,
                                language,
                                manager,
                                eventSystemEnabled));
            } else {
                return createViewProvider(
                        file,
                        language,
                        manager,
                        eventSystemEnabled);
            }
        } else{
            return new SingleRootFileViewProvider(manager, file, eventSystemEnabled);
        }
    }

    @NotNull
    private DatabaseFileViewProvider createViewProvider(@NotNull VirtualFile file, Language language, @NotNull PsiManager manager, boolean eventSystemEnabled) {
        return new DatabaseFileViewProvider(manager.getProject(), file, eventSystemEnabled, language);
    }
}
