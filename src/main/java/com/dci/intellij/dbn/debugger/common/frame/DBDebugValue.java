package com.dci.intellij.dbn.debugger.common.frame;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.thread.Background;
import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.debugger.common.evaluation.DBDebuggerEvaluator;
import com.dci.intellij.dbn.debugger.common.process.DBDebugProcess;
import com.intellij.xdebugger.evaluation.XDebuggerEvaluator;
import com.intellij.xdebugger.frame.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

@Getter
@Setter
public abstract class DBDebugValue<T extends DBDebugStackFrame> extends XNamedValue implements Comparable<DBDebugValue>{
    protected String value;
    protected String type;
    protected Icon icon;
    protected List<String> childVariableNames;

    private final T stackFrame;
    private final DBDebugValue<T> parentValue;

    public DBDebugValue(T stackFrame, String name) {
        super(name);
        this.stackFrame = stackFrame;
        this.parentValue = null;
    }

    protected DBDebugValue(T stackFrame, @NotNull String name, @Nullable List<String> childVariableNames, @Nullable DBDebugValue<T>parentValue, @Nullable Icon icon) {
        super(name);
        this.stackFrame = stackFrame;
        this.parentValue = parentValue;
        if (icon == null) {
            icon = parentValue == null ?
                    Icons.DBO_VARIABLE :
                    Icons.DBO_ATTRIBUTE;
        }
        this.icon = icon;
        this.childVariableNames = childVariableNames;
    }

    @Override
    public void computePresentation(@NotNull final XValueNode node, @NotNull final XValuePlace place) {
        // enabling this will show always variables as changed
        //node.setPresentation(icon, null, "", childVariableNames != null);
        Background.run(null, () -> {
            XDebuggerEvaluator evaluator1 = getStackFrame().getEvaluator();
            DBDebuggerEvaluator<? extends DBDebugStackFrame, DBDebugValue> evaluator = (DBDebuggerEvaluator<? extends DBDebugStackFrame, DBDebugValue>) evaluator1;
            evaluator.computePresentation(DBDebugValue.this, node, place);
        });
    }


    @Override
    public void computeChildren(@NotNull XCompositeNode node) {
        if (childVariableNames != null) {
            for (String childVariableName : childVariableNames) {
                childVariableName = childVariableName.substring(getVariableName().length() + 1);
                XValueChildrenList debugValueChildren = new XValueChildrenList();
                DBDebugValue value = stackFrame.createDebugValue(childVariableName, this, null, null);
                debugValueChildren.add(value);
                node.addChildren(debugValueChildren, true);
            }
        } else {
            super.computeChildren(node);
        }
    }

    public DBDebugProcess getDebugProcess() {
        return stackFrame.getDebugProcess();
    }

    public String getVariableName() {
        return getName();
    }

    public String getDisplayValue() {
        if (value == null) return childVariableNames == null ? "null" : "";
        return isLiteral() && false ? "'" + value + "'" : value;
    }

    public boolean isNumeric() {
        return value != null && Strings.isNumber(value);
    }

    public boolean isBoolean() {
        return value != null && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"));
    }

    public boolean isLiteral() {
        return value != null && !value.isEmpty() && !isNumeric() && !isBoolean();
    }

    public boolean hasChildren() {
        return childVariableNames != null;
    }

    @Override
    public int compareTo(@NotNull DBDebugValue remote) {
        return getName().compareTo(remote.getName());
    }
}

