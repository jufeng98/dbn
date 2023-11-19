package com.dbn.debugger.jdwp.frame;

import com.dbn.common.latent.Latent;
import com.dbn.debugger.DBDebugUtil;
import com.dbn.debugger.common.frame.DBDebugSourcePosition;
import com.dbn.debugger.common.frame.DBDebugStackFrame;
import com.dbn.debugger.jdwp.DBJdwpDebugUtil;
import com.dbn.debugger.jdwp.evaluation.DBJdwpDebuggerEvaluator;
import com.dbn.debugger.jdwp.process.DBJdwpDebugProcess;
import com.dbn.execution.ExecutionInput;
import com.dbn.execution.statement.StatementExecutionInput;
import com.dbn.language.common.psi.IdentifierPsiElement;
import com.dbn.object.common.DBSchemaObject;
import com.intellij.debugger.engine.JavaStackFrame;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.frame.XCompositeNode;
import com.sun.jdi.Location;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

import static com.dbn.common.util.Strings.toLowerCase;

@Getter
public class DBJdwpDebugStackFrame extends DBDebugStackFrame<DBJdwpDebugProcess<?>, DBJdwpDebugValue> {
    private JavaStackFrame underlyingFrame;

    private final Latent<DBJdwpDebuggerEvaluator> evaluator = Latent.basic(() -> new DBJdwpDebuggerEvaluator(DBJdwpDebugStackFrame.this));

    private final Latent<Location> location = Latent.basic(() -> underlyingFrame == null ? null : underlyingFrame.getDescriptor().getLocation());

    DBJdwpDebugStackFrame(DBJdwpDebugProcess debugProcess, JavaStackFrame underlyingFrame, int index) {
        super(debugProcess, index);
        this.underlyingFrame = underlyingFrame;
    }

    @Override
    public void computeChildren(@NotNull XCompositeNode node) {
        DBJdwpCompositeNode wrapper = new DBJdwpCompositeNode(node);
        underlyingFrame.computeChildren(wrapper);
    }

    @Override
    protected XSourcePosition resolveSourcePosition() {
        Location location = getLocation();
        int lineNumber = location == null ? 0 : location.lineNumber() - 1;

        DBJdwpDebugProcess debugProcess = getDebugProcess();
        if (debugProcess.isDeclaredBlock(location) || DBJdwpDebugUtil.getOwnerName(location) == null) {
            ExecutionInput executionInput = debugProcess.getExecutionInput();
            if (executionInput instanceof StatementExecutionInput) {
                StatementExecutionInput statementExecutionInput = (StatementExecutionInput) executionInput;
                lineNumber += statementExecutionInput.getExecutableLineNumber();
            }
        }
        return DBDebugSourcePosition.create(getVirtualFile(), lineNumber);
    }

    @Override
    protected VirtualFile resolveVirtualFile() {
        Location location = getLocation();
        return getDebugProcess().getVirtualFile(location);

    }

    @Override
    @NotNull
    public DBJdwpDebuggerEvaluator getEvaluator() {
        return evaluator.get();
    }


    @Nullable
    public Location getLocation() {
        return location.get();
    }

    @Nullable
    @Override
    protected DBJdwpDebugValue createSuspendReasonDebugValue() {
        return null;
    }

    @NotNull
    @Override
    public DBJdwpDebugValue createDebugValue(String variableName, DBJdwpDebugValue parentValue, List<String> childVariableNames, Icon icon) {
        //return new DBJdwpDebugValue(this, parentValue, variableName, childVariableNames, icon);
        throw new UnsupportedOperationException();
    }

        @Nullable
    @Override
    public Object getEqualityObject() {
        DBSchemaObject object = DBDebugUtil.getObject(getSourcePosition());
        if (object == null) return null;

        IdentifierPsiElement subject = getSubject();
        String subjectString = subject == null ? null : subject.getText();

        return toLowerCase(object.getQualifiedName() + "." + subjectString);
        //return underlyingFrame.getEqualityObject();
    }
}


