package com.dbn.ddl;

import com.dbn.object.common.DBObject;
import com.dbn.object.lookup.DBObjectRef;
import lombok.Getter;

@Getter
public class DDLFileNameProvider {
    private final DBObjectRef object;
    private final DDLFileType ddlFileType;
    private final String extension;

    public DDLFileNameProvider(DBObjectRef object, DDLFileType ddlFileType, String extension) {
        this.object = object;
        this.ddlFileType = ddlFileType;
        this.extension = extension;
    }

    public DBObject getObject() {
        return object.get();
    }

    public String getFileName() {
        return object.getFileName().toLowerCase() + '.' + extension;
    }
}
