package com.dbn.common.locale.options;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface RegionalSettingsListener extends EventListener {
    Topic<RegionalSettingsListener> TOPIC = Topic.create("regional settings change event", RegionalSettingsListener.class);
    void settingsChanged();
}
