package com.dbn.debugger.common.frame;

import com.intellij.xdebugger.frame.XCompositeNode;
import lombok.Getter;
import lombok.experimental.Delegate;

@Getter
public class DBDebugNodeDelegate implements XCompositeNode {

    @Delegate
    private final XCompositeNode delegate;

    public DBDebugNodeDelegate(XCompositeNode delegate) {
        this.delegate = delegate;
    }
}
