package com.dbn.debugger.jdwp.frame;

import com.dbn.debugger.common.frame.DBDebugValueDelegate;
import com.intellij.debugger.engine.JavaValue;
import com.intellij.debugger.jdi.LocalVariableProxyImpl;
import com.intellij.debugger.ui.impl.watch.LocalVariableDescriptorImpl;
import com.intellij.debugger.ui.impl.watch.ValueDescriptorImpl;
import com.intellij.xdebugger.XExpression;
import com.intellij.xdebugger.evaluation.XInstanceEvaluator;
import com.intellij.xdebugger.frame.XValue;
import com.intellij.xdebugger.frame.XValueModifier;
import com.intellij.xdebugger.frame.XValueNode;
import com.intellij.xdebugger.frame.XValuePlace;
import com.sun.jdi.Type;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.concurrency.Promise;

public class DBJdwpDebugValue extends DBDebugValueDelegate<DBJdwpDebugStackFrame> {
    private final DBJdwpDebugValueModifier modifier;


    @SneakyThrows
    public DBJdwpDebugValue(String name, XValue delegate, DBJdwpDebugStackFrame frame) {
        super(name, delegate, frame);
        this.modifier = new DBJdwpDebugValueModifier(this);

        JavaValue javaValue = getJavaValue();
        if (javaValue == null) return;

        ValueDescriptorImpl descriptor = javaValue.getDescriptor();

        if (descriptor instanceof LocalVariableDescriptorImpl) {
            LocalVariableDescriptorImpl localVariableDescriptor = (LocalVariableDescriptorImpl) descriptor;
            LocalVariableProxyImpl localVariable = localVariableDescriptor.getLocalVariable();
            Type type = localVariable.getType();
            setType(type.name());
        }
    }

    @Nullable
    JavaValue getJavaValue() {
        XValue delegate = getDelegate();
        if (delegate instanceof JavaValue) {
            return (JavaValue) delegate;
        }
        return null;
    }


    @Override
    public @Nullable XValueModifier getModifier() {
        // TODO DBN-580 enable alternative modifier
        //return modifier;
        return super.getModifier();
    }

    @Override
    public @Nullable XInstanceEvaluator getInstanceEvaluator() {
        return super.getInstanceEvaluator();
    }

    @Override
    public @NotNull Promise<XExpression> calculateEvaluationExpression() {
        return super.calculateEvaluationExpression();
    }

    @Override
    public @Nullable String getEvaluationExpression() {
        return super.getEvaluationExpression();
    }

    @Override
    public void computePresentation(@NotNull XValueNode node, @NotNull XValuePlace place) {
        super.computePresentation(node, place);
    }
}
