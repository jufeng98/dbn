package com.dbn.data.editor.ui.calendar;

import com.dbn.common.icon.Icons;
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
        CalendarPopupProviderForm form = getCalendarForm(e);
        if (form == null) return;

        Calendar calendar = new GregorianCalendar(2000, Calendar.JANUARY, 1, 0, 0, 0);
        String timeString = form.getFormatter().formatTime(calendar.getTime());
        form.setTimeText(timeString);
    }
}
