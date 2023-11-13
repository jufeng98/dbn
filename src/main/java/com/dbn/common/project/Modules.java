package com.dbn.common.project;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class Modules {

    @Nullable
    public static VirtualFile getModuleContentRoot(@Nullable Module module, VirtualFile file) {
        if (module == null) return null;

        ModuleRootManager rootManager = ModuleRootManager.getInstance(module);
        VirtualFile[] contentRoots = rootManager.getContentRoots();

        while (file != null) {
            file = file.getParent();
            for (VirtualFile contentRoot : contentRoots) {
                if (contentRoot.equals(file)) {
                    return contentRoot;
                }
            }
        }
        return null;
    }
}
