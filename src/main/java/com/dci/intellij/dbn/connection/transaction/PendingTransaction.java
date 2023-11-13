package com.dci.intellij.dbn.connection.transaction;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.ref.WeakRef;
import com.dci.intellij.dbn.connection.SessionId;
import com.dci.intellij.dbn.connection.jdbc.DBNConnection;
import com.dci.intellij.dbn.vfs.DBVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PendingTransaction {
    private final WeakRef<VirtualFile> file;
    private final WeakRef<DBNConnection> connection;
    private int changesCount = 0;

    public PendingTransaction(@NotNull DBNConnection connection, VirtualFile file) {
        this.file = WeakRef.of(file);
        this.connection = WeakRef.of(connection);
    }

    @NotNull
    public DBNConnection getConnection() {
        return connection.ensure();
    }

    @Nullable
    public VirtualFile getFile() {
        return file.get();
    }

    public SessionId getSessionId() {
        return getConnection().getSessionId();
    }

    public int getChangesCount() {
        return changesCount;
    }

    public void incrementChangesCount() {
        changesCount++;
    }

    public String getFilePath() {
        VirtualFile file = getFile();
        return file == null ? "Unknown file" : file.getPresentableUrl();
    }

    public Icon getFileIcon() {
        VirtualFile file = getFile();

        if (file != null) {
            if (file instanceof DBVirtualFile) {
                DBVirtualFile databaseVirtual = (DBVirtualFile) file;
                return databaseVirtual.getIcon();
            } else {
                return file.getFileType().getIcon();
            }
        }
        return Icons.FILE_SQL;
    }
}
