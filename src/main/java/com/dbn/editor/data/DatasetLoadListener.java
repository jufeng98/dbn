package com.dbn.editor.data;

import com.dbn.vfs.DBVirtualFile;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface DatasetLoadListener extends EventListener {
    Topic<DatasetLoadListener> TOPIC = Topic.create("Dataset loaded", DatasetLoadListener.class);

    void datasetLoaded(@NotNull DBVirtualFile virtualFile);
    void datasetLoading(@NotNull DBVirtualFile virtualFile);
}
