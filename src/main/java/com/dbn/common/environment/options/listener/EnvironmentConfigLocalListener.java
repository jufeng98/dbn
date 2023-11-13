package com.dbn.common.environment.options.listener;

import com.dbn.common.environment.EnvironmentTypeBundle;
import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface EnvironmentConfigLocalListener extends EventListener {
    Topic<EnvironmentConfigLocalListener> TOPIC = Topic.create("EnvironmentConfigListener", EnvironmentConfigLocalListener.class);
    void settingsChanged(EnvironmentTypeBundle environmentTypes);
}
