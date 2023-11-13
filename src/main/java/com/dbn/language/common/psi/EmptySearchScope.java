package com.dbn.language.common.psi;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

public class EmptySearchScope extends GlobalSearchScope {
    public static final EmptySearchScope INSTANCE = new EmptySearchScope();    

    @Override
    public int compare(@NotNull final VirtualFile file1, @NotNull final VirtualFile file2) {
        return 0;
    }

    @Override
    public boolean contains(@NotNull final VirtualFile file) {
        return false;
    }

    @Override
    public boolean isSearchInLibraries() {
        return false;
    }

    @Override
    public boolean isSearchInModuleContent(@NotNull final Module aModule) {
        return false;
    }
}