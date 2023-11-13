package com.dbn.connection.console;

import com.dbn.common.thread.Write;
import com.dbn.editor.code.content.GuardedBlockMarkers;
import com.dbn.editor.code.content.GuardedBlockType;
import com.dbn.vfs.file.DBConsoleVirtualFile;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManagerAdapter;
import com.intellij.openapi.fileEditor.FileDocumentManagerListener;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import static com.dbn.common.util.GuardedBlocks.createGuardedBlocks;
import static com.dbn.common.util.GuardedBlocks.removeGuardedBlocks;

public class DatabaseConsoleFileInitializer extends FileDocumentManagerAdapter implements FileDocumentManagerListener {
    @Override
    public void fileContentLoaded(@NotNull VirtualFile file, @NotNull Document document) {
        if (file instanceof DBConsoleVirtualFile) {
            // restore guarded blocks after console file loaded
            DBConsoleVirtualFile consoleFile = (DBConsoleVirtualFile) file;
            GuardedBlockMarkers guardedBlocks = consoleFile.getContent().getOffsets().getGuardedBlocks();
            if (guardedBlocks.isEmpty()) return;

            Write.run(() -> {
                removeGuardedBlocks(document, GuardedBlockType.READONLY_DOCUMENT_SECTION);
                createGuardedBlocks(document, GuardedBlockType.READONLY_DOCUMENT_SECTION, guardedBlocks, null);
            });
        }
    }
}
