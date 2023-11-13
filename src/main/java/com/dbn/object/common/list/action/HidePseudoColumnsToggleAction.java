package com.dbn.object.common.list.action;

import com.dbn.browser.options.ObjectFilterChangeListener;
import com.dbn.common.constant.Constant;
import com.dbn.common.event.ProjectEvents;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionId;
import com.dbn.connection.action.AbstractConnectionToggleAction;
import com.dbn.object.type.DBObjectType;
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
