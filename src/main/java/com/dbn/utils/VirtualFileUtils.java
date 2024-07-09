package com.dbn.utils;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class VirtualFileUtils {

    @SuppressWarnings("DataFlowIssue")
    @SneakyThrows
    public static VirtualFile createVirtualSqlFileFromText(byte[] txtBytes) {
        Path tempFile;
        tempFile = Files.createTempFile("dbn-", ".mysql");
        File file = tempFile.toFile();
        VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(file.getAbsolutePath());
        virtualFile.setBinaryContent(txtBytes);
        return virtualFile;
    }

}

