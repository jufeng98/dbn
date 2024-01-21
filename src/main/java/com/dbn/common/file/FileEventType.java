package com.dbn.common.file;

import com.dbn.common.constant.Constant;

public enum FileEventType implements Constant<FileEventType> {
    CREATED,
    DELETED,
    MODIFIED,
    RENAMED,
    MOVED,

    UNKNOWN
}
