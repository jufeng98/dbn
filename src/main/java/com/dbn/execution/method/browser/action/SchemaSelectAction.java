package com.dbn.execution.method.browser.action;

import com.dbn.common.ref.WeakRef;
import com.dbn.execution.method.browser.ui.MethodExecutionBrowserForm;
import com.dbn.object.DBSchema;
import com.dbn.object.action.AnObjectAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class SchemaSelectAction extends AnObjectAction<DBSchema> {
    private WeakRef<MethodExecutionBrowserForm> browserComponent;

    SchemaSelectAction(MethodExecutionBrowserForm browserComponent, DBSchema schema) {
        super(schema);
        this.browserComponent = WeakRef.of(browserComponent);
    }

    @Override
    protected void actionPerformed(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull DBSchema object) {

        MethodExecutionBrowserForm browserForm = browserComponent.get();
        if (browserForm != null) {
            browserForm.setSchema(object);
        }
    }


}
