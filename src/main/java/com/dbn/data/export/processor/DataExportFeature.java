package com.dbn.data.export.processor;

import com.dbn.common.constant.Constant;
import org.jetbrains.annotations.Nullable;

public enum DataExportFeature implements Constant<DataExportFeature> {
    HEADER_CREATION,
    FRIENDLY_HEADER,
    EXPORT_TO_FILE,
    EXPORT_TO_CLIPBOARD,
    VALUE_QUOTING,
    FILE_ENCODING;

    public boolean isSupported(@Nullable DataExportProcessor processor) {
        return processor != null && processor.supports(this);
    }
}
