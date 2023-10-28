package com.dci.intellij.dbn.data.editor.ui.calendar;

import com.dci.intellij.dbn.common.Icons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/******************************************************
 *                       Actions                      *
 ******************************************************/
public class CalendarNextMonthAction extends CalendarPopupAction {
    CalendarNextMonthAction() {
        super("Next Month", null, Icons.CALENDAR_NEXT_MONTH);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        getCalendarTableModel(e).rollMonth(1);
    }
}
