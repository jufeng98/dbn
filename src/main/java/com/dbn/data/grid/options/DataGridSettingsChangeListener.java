package com.dbn.data.grid.options;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface DataGridSettingsChangeListener extends EventListener {
    Topic<DataGridSettingsChangeListener> TOPIC = Topic.create("Data Grid settings change event", DataGridSettingsChangeListener.class);
    void auditDataVisibilityChanged(boolean visible);
}
