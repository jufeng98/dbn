package com.dbn.language.common;

import com.dbn.common.util.Strings;
import com.dbn.editor.DBContentType;
import com.dbn.vfs.file.DBConsoleVirtualFile;
import com.dbn.vfs.file.DBEditableObjectVirtualFile;
import com.dbn.vfs.file.DBSourceCodeVirtualFile;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@Getter
public abstract class DBLanguageFileType extends LanguageFileType implements FileTypeIdentifiableByVirtualFile {
    private final String defaultExtension;
    private final String[] supportedExtensions;
    private final String description;
    private final DBContentType contentType;

    public DBLanguageFileType(
            @NotNull Language language,
            @NotNull String[] supportedExtensions,
            @NotNull String description,
            @NotNull DBContentType contentType) {
        super(language);
        this.supportedExtensions = supportedExtensions;
        this.defaultExtension = supportedExtensions[0];
        this.description = description;
        this.contentType = contentType;
    }

    @Override
    @NotNull
    public String getName() {
        return getLanguage().getID();
    }

    @Override
    public boolean isMyFileType(@NotNull VirtualFile file) {
        if (file instanceof DBEditableObjectVirtualFile || file instanceof DBSourceCodeVirtualFile) {
            if (this == file.getFileType()) {
                return true;
            }
        }

        if (file instanceof DBConsoleVirtualFile) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return getLanguage().getID();
    }

    public boolean isSupported(String extension) {
        return Arrays.stream(supportedExtensions).anyMatch(e -> Strings.equalsIgnoreCase(e, extension));
    }
}
