package com.dci.intellij.dbn.data.editor.ui.calendar;

import com.dci.intellij.dbn.common.Icons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class CalendarPreviousMonthAction extends CalendarPopupAction {
    CalendarPreviousMonthAction() {
        super("Previous Month", null, Icons.CALENDAR_PREVIOUS_MONTH);

    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        getCalendarTableModel(e).rollMonth(-1);
    }
}
