package com.dbn.browser.model;

import com.dbn.common.ui.tree.TreeEventType;
import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface BrowserTreeEventListener extends EventListener{
    Topic<BrowserTreeEventListener> TOPIC = Topic.create("Browser tree event", BrowserTreeEventListener.class);

    default void nodeChanged(BrowserTreeNode node, TreeEventType eventType) {};

    default void selectionChanged(){};
}
