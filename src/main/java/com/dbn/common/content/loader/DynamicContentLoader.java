package com.dbn.common.content.loader;

import com.dbn.common.content.DynamicContent;
import com.dbn.common.content.DynamicContentElement;
import com.dbn.database.common.metadata.DBObjectMetadata;

import java.sql.SQLException;

public interface DynamicContentLoader<T extends DynamicContentElement, M extends DBObjectMetadata> {
    void loadContent(DynamicContent<T> content) throws SQLException;

    DynamicContentLoader VOID_CONTENT_LOADER = (content) -> {};
}
