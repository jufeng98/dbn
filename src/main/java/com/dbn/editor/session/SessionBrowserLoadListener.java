package com.dbn.editor.session;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface SessionBrowserLoadListener extends EventListener {
    Topic<SessionBrowserLoadListener> TOPIC = Topic.create("Sessions loaded", SessionBrowserLoadListener.class);

    void sessionsLoaded(VirtualFile virtualFile);
}
