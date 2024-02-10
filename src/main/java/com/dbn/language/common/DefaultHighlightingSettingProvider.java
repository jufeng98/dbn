package com.dbn.language.common;

import com.dbn.vfs.DBVirtualFile;
import com.dbn.vfs.file.DBConsoleVirtualFile;
import com.dbn.vfs.file.DBSourceCodeVirtualFile;
import com.intellij.codeInsight.daemon.impl.analysis.FileHighlightingSetting;
import com.intellij.ide.EssentialHighlightingMode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultHighlightingSettingProvider extends com.intellij.codeInsight.daemon.impl.analysis.DefaultHighlightingSettingProvider {
    @Override
    public @Nullable FileHighlightingSetting getDefaultSetting(@NotNull Project project, @NotNull VirtualFile file) {
        if (file instanceof DBSourceCodeVirtualFile) return getDefaultSetting();
        if (file instanceof DBConsoleVirtualFile) return getDefaultSetting();
        if (file instanceof DBVirtualFile) return FileHighlightingSetting.SKIP_HIGHLIGHTING;

        return null;
    }

    @NotNull
    private FileHighlightingSetting getDefaultSetting() {
        return EssentialHighlightingMode.INSTANCE.isEnabled() ?
                FileHighlightingSetting.ESSENTIAL :
                FileHighlightingSetting.FORCE_HIGHLIGHTING;
    }
}
