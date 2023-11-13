package com.dbn.data.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
public class DataModelState {
    protected Map<String, String> contentTypesMap;
    private boolean readonly;
    private int rowCount;

    public void setTextContentType(String columnName, String contentTypeName) {
        if (contentTypesMap == null) contentTypesMap = new HashMap<>();
        contentTypesMap.put(columnName, contentTypeName);
    }

    public String getTextContentTypeName(String columnName) {
        if (contentTypesMap != null) {
            return contentTypesMap.get(columnName);
        }
        return null;
    }
}
