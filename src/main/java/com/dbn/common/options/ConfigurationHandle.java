package com.dbn.common.options;

import com.dbn.common.dispose.Failsafe;
import com.dbn.common.thread.ThreadLocalFlag;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

@Slf4j
public final class ConfigurationHandle {
    private static final ThreadLocalFlag IS_TRANSITORY = new ThreadLocalFlag(false);
    private static final ThreadLocalFlag IS_RESETTING = new ThreadLocalFlag(false);
    private static final ThreadLocal<List<SettingsChangeNotifier>> SETTINGS_CHANGE_NOTIFIERS = new ThreadLocal<>();

    public static boolean isTransitory() {
        return IS_TRANSITORY.get();
    }

    public static void setTransitory(boolean transitory) {
        IS_TRANSITORY.set(transitory);
    }

    public static boolean isResetting() {
        return IS_RESETTING.get();
    }

    public static void setResetting(boolean transitory) {
        IS_RESETTING.set(transitory);
    }

    public static void registerChangeNotifier(SettingsChangeNotifier notifier) {
        List<SettingsChangeNotifier> notifiers = SETTINGS_CHANGE_NOTIFIERS.get();
        if (notifiers == null) {
            notifiers = new ArrayList<>();
            SETTINGS_CHANGE_NOTIFIERS.set(notifiers);
        }
        notifiers.add(notifier);
    }

    public static void notifyChanges() {
        List<SettingsChangeNotifier> changeNotifiers = SETTINGS_CHANGE_NOTIFIERS.get();
        if (changeNotifiers != null) {
            SETTINGS_CHANGE_NOTIFIERS.remove();
            for (SettingsChangeNotifier changeNotifier : changeNotifiers) {
                try {
                    Failsafe.guarded(() -> changeNotifier.notifyChanges());
                } catch (Exception e){
                    conditionallyLog(e);
                    log.error("Error notifying configuration changes", e);
                }
            }
        }
    }
}
