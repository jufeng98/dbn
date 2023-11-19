package com.dbn.ddl;

import com.dbn.object.common.DBObject;
import com.dbn.object.lookup.DBObjectRef;
import lombok.Getter;

import static com.dbn.common.util.Strings.toLowerCase;

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
        return toLowerCase(object.getFileName()) + '.' + extension;
    }
}
