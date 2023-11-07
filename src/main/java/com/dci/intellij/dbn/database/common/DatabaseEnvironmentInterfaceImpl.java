package com.dci.intellij.dbn.database.common;

import com.dci.intellij.dbn.database.interfaces.DatabaseEnvironmentInterface;

public class DatabaseEnvironmentInterfaceImpl implements DatabaseEnvironmentInterface {
    @Override
    public boolean isCloudDatabase(String hostname) {
        return false;
    }
}
