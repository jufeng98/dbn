package com.dbn.debugger.common.frame;

import com.intellij.xdebugger.frame.XValue;
import lombok.Getter;
import lombok.experimental.Delegate;

@Getter
public class DBDebugValueDelegate<T extends DBDebugStackFrame<?,?>> extends DBDebugValue<T> {

    @Delegate
    private final XValue delegate;

    public DBDebugValueDelegate(String name, XValue value, T frame) {
        super(frame, name);
        this.delegate = value;
    }
}
