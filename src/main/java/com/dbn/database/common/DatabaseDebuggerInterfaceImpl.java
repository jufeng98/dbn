package com.dbn.database.common;

import com.dbn.database.interfaces.DatabaseDebuggerInterface;
import com.dbn.database.interfaces.DatabaseInterfaces;

public abstract class DatabaseDebuggerInterfaceImpl extends DatabaseInterfaceBase implements DatabaseDebuggerInterface {
    public DatabaseDebuggerInterfaceImpl(String fileName, DatabaseInterfaces provider) {
        super(fileName, provider);
    }


}
