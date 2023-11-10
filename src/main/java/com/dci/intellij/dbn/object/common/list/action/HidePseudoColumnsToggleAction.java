package com.dci.intellij.dbn.object.common.list.action;

import com.dci.intellij.dbn.browser.options.ObjectFilterChangeListener;
import com.dci.intellij.dbn.common.constant.Constant;
import com.dci.intellij.dbn.common.event.ProjectEvents;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionId;
import com.dci.intellij.dbn.connection.action.AbstractConnectionToggleAction;
import com.dci.intellij.dbn.object.type.DBObjectType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class HidePseudoColumnsToggleAction extends AbstractConnectionToggleAction {

    public HidePseudoColumnsToggleAction(ConnectionHandler connection) {
        super("Hide pseudo columns", connection);
    }

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        ConnectionHandler connection = getConnection();
        return connection.getSettings().getFilterSettings().isHidePseudoColumns();
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        ConnectionHandler connection = getConnection();
        connection.getSettings().getFilterSettings().setHidePseudoColumns(state);
        ConnectionId connectionId = connection.getConnectionId();
        ProjectEvents.notify(
                connection.getProject(),
                ObjectFilterChangeListener.TOPIC,
                (listener) -> listener.nameFiltersChanged(connectionId, Constant.array(DBObjectType.COLUMN)));

    }
}
