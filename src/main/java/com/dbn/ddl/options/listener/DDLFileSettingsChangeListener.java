package com.dbn.ddl.options.listener;

import com.intellij.openapi.project.Project;
import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface DDLFileSettingsChangeListener extends EventListener {
    Topic<DDLFileSettingsChangeListener> TOPIC = Topic.create("DDLFileSettingsEvents", DDLFileSettingsChangeListener.class);
    void settingsChanged(Project project);
}
