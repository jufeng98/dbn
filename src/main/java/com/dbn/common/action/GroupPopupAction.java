package com.dbn.common.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;

public abstract class GroupPopupAction extends ProjectAction {
    private final String groupTitle;
    public GroupPopupAction(String name, @Nullable String groupTitle, @Nullable Icon icon) {
        super(name, null, icon);
        this.groupTitle = groupTitle;
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        DefaultActionGroup actionGroup = new DefaultActionGroup();

        for (AnAction action : getActions(e)) {
            actionGroup.add(action);
        }
        InputEvent inputEvent = e.getInputEvent();
        if (inputEvent != null) {
            Component component = (Component) inputEvent.getSource();
            if (component.isShowing()) {
                ListPopup popup = JBPopupFactory.getInstance().createActionGroupPopup(
                        groupTitle,
                        actionGroup,
                        e.getDataContext(),
                        JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                        true, null, 10);

                //Project project = (Project) e.getDataContext().getData(DataConstants.PROJECT);
                DataProvider dataProvider = getDataProvider(e);
                if (dataProvider != null) {
                    DataProviders.register(popup.getContent(), dataProvider);
                }
                showBelowComponent(popup, component);
            }
        }
    }

    public DataProvider getDataProvider(AnActionEvent e) {
        return null;
    }

    private static void showBelowComponent(ListPopup popup, Component component) {
        Point locationOnScreen = component.getLocationOnScreen();
        Point location = new Point(
                (int) (locationOnScreen.getX() + 10),
                (int) locationOnScreen.getY() + component.getHeight());
        popup.showInScreenCoordinates(component, location);
    }

    protected abstract AnAction[] getActions(AnActionEvent e);
}
