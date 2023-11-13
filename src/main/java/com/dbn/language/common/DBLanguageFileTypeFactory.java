package com.dbn.language.common;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

@Deprecated
public abstract class DBLanguageFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer consumer) {
        FileType fileType = getFileType();
        consumer.consume(fileType, fileType.getDefaultExtension());
    }

    protected abstract FileType getFileType();
}
