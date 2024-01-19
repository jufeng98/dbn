package com.dbn.common.file;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.events.*;
import lombok.Getter;

@Getter
public class FileMappingEvent<T> {
    private final T target;
    private final VirtualFile file;
    private final FileEventType eventType;

    public FileMappingEvent(T target, VirtualFile file, FileEventType eventType) {
        this.target = target;
        this.file = file;
        this.eventType = eventType;
    }

    public FileMappingEvent(T target, VFileEvent fileEvent) {
        this.target = target;
        this.file = fileEvent.getFile();
        if (fileEvent instanceof VFileDeleteEvent) {
            eventType = FileEventType.DELETED;
        } else if (fileEvent instanceof VFileMoveEvent) {
            eventType = FileEventType.MOVED;
        } else if (fileEvent instanceof VFileCreateEvent) {
            eventType = FileEventType.CREATED;
        } else if (fileEvent instanceof VFileContentChangeEvent) {
            eventType = FileEventType.MODIFIED;
        } else if (fileEvent instanceof VFilePropertyChangeEvent) {
            VFilePropertyChangeEvent propertyChangeEvent = (VFilePropertyChangeEvent) fileEvent;
            String propertyName = propertyChangeEvent.getPropertyName();
            if (VirtualFile.PROP_NAME.equals(propertyName)) {
                eventType = FileEventType.RENAMED;
            } else {
                eventType = FileEventType.MODIFIED;
            }
        } else {
            eventType = FileEventType.UNKNOWN;
        }

    }
}
