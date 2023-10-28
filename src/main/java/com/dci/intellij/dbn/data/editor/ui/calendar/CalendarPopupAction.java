package com.dci.intellij.dbn.data.editor.ui.calendar;

import com.dci.intellij.dbn.common.action.BasicAction;
import com.dci.intellij.dbn.common.action.DataKeys;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class CalendarPopupAction extends BasicAction {
    public CalendarPopupAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    protected CalendarPopupProviderForm getCalendarForm(@NotNull AnActionEvent e) {
        return e.getData(DataKeys.CALENDAR_POPUP_PROVIDER_FORM);
    }

    CalendarTableModel getCalendarTableModel(@NotNull AnActionEvent e) {
        CalendarPopupProviderForm form = getCalendarForm(e);
        return form.getCalendarTableModel();
    }
}
