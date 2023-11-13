package com.dbn.common.ui.util;

import com.intellij.openapi.ui.SelectFromListDialog;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

@Slf4j
@UtilityClass
public final class Lists {

    public static void notifyListDataListeners(Object source, Listeners<ListDataListener> listeners, int fromIndex, int toIndex, int eventType) {
        try {
            ListDataEvent event = new ListDataEvent(source, eventType, fromIndex, toIndex);
            listeners.notify(l -> {
                switch (eventType) {
                    case ListDataEvent.INTERVAL_ADDED:   l.intervalAdded(event); break;
                    case ListDataEvent.INTERVAL_REMOVED: l.intervalRemoved(event); break;
                    case ListDataEvent.CONTENTS_CHANGED: l.contentsChanged(event); break;
                }
            });
        } catch (Exception e) {
            conditionallyLog(e);
            log.error("Error notifying actions model listeners", e);
        }
    }

    public static final SelectFromListDialog.ToStringAspect BASIC_TO_STRING_ASPECT = obj -> obj.toString();

}
