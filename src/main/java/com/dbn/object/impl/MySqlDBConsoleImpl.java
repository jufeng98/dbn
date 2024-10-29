package com.dbn.object.impl;

import com.dbn.common.icon.Icons;
import com.dbn.connection.ConnectionHandler;
import com.dbn.vfs.DBConsoleType;
import com.dbn.vfs.file.DBConsoleVirtualFile;
import com.dbn.vfs.file.MySqlDBConsoleVirtualFile;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.dbn.common.dispose.Failsafe.nd;

/**
 * @author yudong
 */
@Getter
public class MySqlDBConsoleImpl extends DBConsoleImpl {
    private final DBConsoleVirtualFile virtualFile;
    private final DBConsoleType consoleType;

    public MySqlDBConsoleImpl(@NotNull ConnectionHandler connection, String name, DBConsoleType consoleType) {
        super(connection, name, consoleType);
        virtualFile = new MySqlDBConsoleVirtualFile(this);
        this.consoleType = consoleType;
    }

    @NotNull
    public DBConsoleVirtualFile getVirtualFile() {
        return nd(virtualFile);
    }

    @Nullable
    @Override
    public Icon getIcon() {
        switch (consoleType) {
            case MYSQL:
                return Icons.DBO_CONSOLE;
            case DEBUG:
                return Icons.DBO_CONSOLE_DEBUG;
        }
        return super.getIcon();
    }

    @Override
    public void setName(String newName) {
        ref().setObjectName(newName);
        virtualFile.setName(newName);
    }
}
