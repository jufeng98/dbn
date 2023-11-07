package com.dci.intellij.dbn.database.oracle;

import com.dci.intellij.dbn.database.interfaces.DatabaseEnvironmentInterface;

public class OracleEnvironmentInterface implements DatabaseEnvironmentInterface {
    @Override
    public boolean isCloudDatabase(String hostname) {
        return false;
    }
}
