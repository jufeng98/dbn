package com.dbn.browser.options.listener;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface ObjectDetailSettingsListener extends EventListener {
    Topic<ObjectDetailSettingsListener> TOPIC = Topic.create("Object Detail Settings", ObjectDetailSettingsListener.class);
    void displayDetailsChanged();
}
