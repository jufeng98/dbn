package com.dci.intellij.dbn.execution.method.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jdom.Element;

import com.dci.intellij.dbn.common.dispose.DisposerUtil;
import com.dci.intellij.dbn.common.options.setting.SettingsUtil;
import com.dci.intellij.dbn.common.state.PersistentStateElement;
import com.dci.intellij.dbn.execution.method.MethodExecutionInput;
import com.dci.intellij.dbn.object.DBMethod;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.intellij.openapi.Disposable;

public class MethodExecutionHistory implements PersistentStateElement<Element>, Disposable{
    private List<MethodExecutionInput> executionInputs = new ArrayList<MethodExecutionInput>();
    private boolean groupEntries = true;
    private DBObjectRef<DBMethod> selection;

    public List<MethodExecutionInput> getExecutionInputs() {
        return executionInputs;
    }

    public void setExecutionInputs(List<MethodExecutionInput> executionInputs) {
        this.executionInputs = executionInputs;
    }

    public boolean isGroupEntries() {
        return groupEntries;
    }

    public void setGroupEntries(boolean groupEntries) {
        this.groupEntries = groupEntries;
    }

    public DBObjectRef<DBMethod> getSelection() {
        return selection;
    }

    public void setSelection(DBObjectRef<DBMethod> selection) {
        this.selection = selection;
    }

    public void cleanupHistory(List<String> connectionIds) {
        Iterator<MethodExecutionInput> iterator = executionInputs.iterator();
        while (iterator.hasNext()) {
            MethodExecutionInput executionInput = iterator.next();
            if (connectionIds.contains(executionInput.getConnectionId())) {
                iterator.remove();
            }
        }

    }

    public MethodExecutionInput getExecutionInput(DBMethod method) {
        for (MethodExecutionInput executionInput : executionInputs) {
            if (executionInput.getMethodRef().is(method)) {
                return executionInput;
            }
        }
        MethodExecutionInput executionInput = new MethodExecutionInput(method);
        executionInputs.add(executionInput);
        Collections.sort(executionInputs);
        selection = DBObjectRef.from(method);
        return executionInput;
    }

    public MethodExecutionInput getExecutionInput(DBObjectRef<DBMethod> methodRef) {
        for (MethodExecutionInput executionInput : executionInputs) {
            if (executionInput.getMethodRef().equals(methodRef)) {
                return executionInput;
            }
        }

        DBMethod method = methodRef.get();
        if (method != null) {
            MethodExecutionInput executionInput = new MethodExecutionInput(method);
            executionInputs.add(executionInput);
            Collections.sort(executionInputs);
            selection = methodRef;
            return executionInput;
        }

        return null;
    }

    @Override
    public void dispose() {
        DisposerUtil.dispose(executionInputs);
    }

    public MethodExecutionInput getLastSelection() {
        if (selection != null) {
            for (MethodExecutionInput executionInput : executionInputs) {
                if (executionInput.getMethodRef().equals(selection)) {
                    return executionInput;
                }
            }
        }
        return null;
    }


    /*****************************************
     *         PersistentStateElement        *
     ****************************************
     * @param element*/
    public void readState(Element element) {
        Element historyElement = element.getChild("execution-history");
        if (historyElement != null) {
            groupEntries = SettingsUtil.getBoolean(historyElement, "group-entries", groupEntries);

            Element executionInputsElement = historyElement.getChild("execution-inputs");
            for (Object object : executionInputsElement.getChildren()) {
                Element configElement = (Element) object;
                MethodExecutionInput executionInput = new MethodExecutionInput();
                executionInput.readConfiguration(configElement);
                executionInputs.add(executionInput);
            }
            Collections.sort(executionInputs);

            Element selectionElement = historyElement.getChild("selection");
            if (selectionElement != null) {
                selection = new DBObjectRef<DBMethod>();
                selection.readState(selectionElement);
            }
        }
    }

    public void writeState(Element element) {
        Element historyElement = new Element("execution-history");
        element.addContent(historyElement);

        SettingsUtil.setBoolean(historyElement, "group-entries", groupEntries);

        Element configsElement = new Element("execution-inputs");
        historyElement.addContent(configsElement);
        for (MethodExecutionInput executionInput : this.executionInputs) {
            if (!executionInput.isObsolete()) {
                Element configElement = new Element("execution-input");
                executionInput.writeConfiguration(configElement);
                configsElement.addContent(configElement);
            }
        }

        if (selection != null) {
            Element selectionElement = new Element("selection");
            historyElement.addContent(selectionElement);
            selection.writeState(selectionElement);
        }

    }
}
