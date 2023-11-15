package com.dbn.data.editor.ui.calendar;

import com.dbn.common.icon.Icons;
import com.dbn.common.util.Safe;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class CalendarPreviousYearAction extends CalendarPopupAction {
    CalendarPreviousYearAction() {
        super("Previous Year", null, Icons.CALENDAR_PREVIOUS_YEAR);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        CalendarTableModel model = getCalendarTableModel(e);
        Safe.run(model, m -> m.rollYear(-1));
    }
}
