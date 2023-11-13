package com.dbn.data.editor.ui.calendar;

import com.dbn.common.icon.Icons;
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
