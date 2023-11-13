package com.dci.intellij.dbn.execution.method.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.execution.method.MethodExecutionManager;
import com.dci.intellij.dbn.execution.method.ui.MethodExecutionHistory;
import com.dci.intellij.dbn.object.DBMethod;
import com.dci.intellij.dbn.object.DBProgram;
import com.dci.intellij.dbn.object.action.ObjectListShowAction;
import com.dci.intellij.dbn.object.common.DBObject;
import com.intellij.openapi.actionSystem.AnAction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.dci.intellij.dbn.common.util.Unsafe.cast;

public class ProgramMethodRunAction extends ObjectListShowAction {
    public ProgramMethodRunAction(DBProgram program) {
        super("Run...", program);
        getTemplatePresentation().setIcon(Icons.METHOD_EXECUTION_RUN);
    }

    @Nullable
    @Override
    public List<DBObject> getRecentObjectList() {
        DBProgram program = (DBProgram) getSourceObject();
        MethodExecutionManager methodExecutionManager = MethodExecutionManager.getInstance(program.getProject());
        MethodExecutionHistory executionHistory = methodExecutionManager.getExecutionHistory();
        return cast(executionHistory.getRecentlyExecutedMethods(program));
    }


    @Override
    public List<DBObject> getObjectList() {
        DBProgram program = (DBProgram) getSourceObject();
        List<DBObject> objects = new ArrayList<>();
        objects.addAll(program.getProcedures());
        objects.addAll(program.getFunctions());
        return objects;
    }

    @Override
    public String getTitle() {
        return "Select method to execute";
    }

    @Override
    public String getEmptyListMessage() {
        DBProgram program = (DBProgram) getSourceObject();
        return "The " + program.getQualifiedNameWithType() + " has no methods to execute.";
    }


    @Override
    public String getListName() {
       return "executable elements";
   }

    @Override
    protected AnAction createObjectAction(DBObject object) {
        return new MethodRunAction((DBProgram) getSourceObject(), (DBMethod) object);
    }
}