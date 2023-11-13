package com.dbn.database.common;

import com.dbn.database.interfaces.DatabaseEnvironmentInterface;

public class DatabaseEnvironmentInterfaceImpl implements DatabaseEnvironmentInterface {
    @Override
    public boolean isCloudDatabase(String hostname) {
        return false;
    }
}
