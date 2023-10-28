package com.dci.intellij.dbn.data.editor.ui.calendar;

import com.dci.intellij.dbn.common.Icons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class CalendarPreviousYearAction extends CalendarPopupAction {
    CalendarPreviousYearAction() {
        super("Previous Year", null, Icons.CALENDAR_PREVIOUS_YEAR);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        getCalendarTableModel(e).rollYear(-1);
    }
}
