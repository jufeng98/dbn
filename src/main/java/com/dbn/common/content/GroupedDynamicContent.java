package com.dbn.common.content;

import com.dbn.connection.DatabaseEntity;

import java.util.List;

public interface GroupedDynamicContent<T extends DynamicContentElement> extends DynamicContent<T> {
    List<T> getChildElements(DatabaseEntity entity);
}
