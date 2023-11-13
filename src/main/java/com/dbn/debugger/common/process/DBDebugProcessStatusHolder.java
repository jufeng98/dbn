package com.dbn.debugger.common.process;

import com.dbn.common.property.PropertyHolderBase;

public class DBDebugProcessStatusHolder extends PropertyHolderBase.IntStore<DBDebugProcessStatus> {

    @Override
    protected DBDebugProcessStatus[] properties() {
        return DBDebugProcessStatus.VALUES;
    }
}
