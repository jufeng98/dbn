package com.dbn.object;

import com.dbn.object.common.DBObject;
import com.dbn.vfs.DBConsoleType;
import com.dbn.vfs.file.DBConsoleVirtualFile;
import org.jetbrains.annotations.NotNull;

public interface DBConsole extends DBObject {
    void setName(String newName);

    @NotNull
    @Override
    DBConsoleVirtualFile getVirtualFile();

    DBConsoleType getConsoleType();
}
