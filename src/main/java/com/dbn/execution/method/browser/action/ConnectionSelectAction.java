package com.dbn.execution.method.browser.action;

import com.dbn.connection.ConnectionHandler;
import com.dbn.execution.method.browser.ui.MethodExecutionBrowserForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

public class ConnectionSelectAction extends DumbAwareAction {
    private final ConnectionHandler connection;
    private MethodExecutionBrowserForm browserComponent;

    ConnectionSelectAction(MethodExecutionBrowserForm browserComponent, ConnectionHandler connection) {
        super();
        this.browserComponent = browserComponent;
        this.connection = connection;
        getTemplatePresentation().setText(connection.getName(), false);
        getTemplatePresentation().setIcon(connection.getIcon());

    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        browserComponent.setConnectionHandler(connection);
    }


}
