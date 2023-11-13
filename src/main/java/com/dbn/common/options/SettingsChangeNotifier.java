package com.dbn.common.options;

public abstract class SettingsChangeNotifier {
    private SettingsChangeNotifier() {
        ConfigurationHandle.registerChangeNotifier(this);
    }

    public abstract void notifyChanges();

    public static void register(Runnable runnable) {
        new SettingsChangeNotifier() {
            @Override
            public void notifyChanges() {
                runnable.run();
            }
        };
    }
}
