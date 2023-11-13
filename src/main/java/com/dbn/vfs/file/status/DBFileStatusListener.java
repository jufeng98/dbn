package com.dbn.vfs.file.status;

import com.dbn.vfs.file.DBContentVirtualFile;
import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface DBFileStatusListener extends EventListener {
    Topic<DBFileStatusListener> TOPIC = Topic.create("File status change event", DBFileStatusListener.class);

    void statusChanged(DBContentVirtualFile file, DBFileStatus status, boolean value);
}
