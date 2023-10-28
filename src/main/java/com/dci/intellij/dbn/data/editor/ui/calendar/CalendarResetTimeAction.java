package com.dci.intellij.dbn.data.editor.ui.calendar;

import com.dci.intellij.dbn.common.Icons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.GregorianCalendar;

class CalendarResetTimeAction extends CalendarPopupAction {
    CalendarResetTimeAction() {
        super("Reset Time", null, Icons.CALENDAR_CLEAR_TIME);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Calendar calendar = new GregorianCalendar(2000, Calendar.JANUARY, 0, 0, 0, 0);
        CalendarPopupProviderForm form = getCalendarForm(e);
        String timeString = form.getFormatter().formatTime(calendar.getTime());
        form.setTimeText(timeString);
    }
}
