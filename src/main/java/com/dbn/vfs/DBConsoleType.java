package com.dbn.vfs;

import com.dbn.common.icon.Icons;

import javax.swing.*;

public enum DBConsoleType {
    STANDARD("SQL Console", Icons.FILE_SQL_CONSOLE),
    DEBUG("Debug Console", Icons.FILE_SQL_DEBUG_CONSOLE);

    private String name;
    private Icon icon;

    DBConsoleType(String name, Icon icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public Icon getIcon() {
        return icon;
    }
}
