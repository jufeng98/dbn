package com.dbn.debugger.jdwp.frame;

import com.dbn.debugger.common.frame.DBDebugNodeDelegate;
import com.intellij.xdebugger.frame.XCompositeNode;
import com.intellij.xdebugger.frame.XValue;
import com.intellij.xdebugger.frame.XValueChildrenList;
import org.jetbrains.annotations.NotNull;

public class DBJdwpCompositeNode extends DBDebugNodeDelegate {
    public DBJdwpCompositeNode(XCompositeNode delegate) {
        super(delegate);
    }

    @Override
    public void addChildren(@NotNull XValueChildrenList children, boolean last) {
        XValueChildrenList wrappedChildren = wrappedChildren(children);
        super.addChildren(wrappedChildren, last);
    }

    @NotNull
    private static XValueChildrenList wrappedChildren(@NotNull XValueChildrenList children) {
        XValueChildrenList wrappedChildren = new XValueChildrenList();

        DBJdwpDebugStackFrame stackFrame = null;

        int size = children.size();
        for (int i = 0; i < size; i++) {
            String name = children.getName(i);
            XValue value = children.getValue(i);
            XValue valueDelegate = new DBJdwpDebugValue(name, value, stackFrame);
            wrappedChildren.add(name, valueDelegate);
        }
        return wrappedChildren;
    }
}
