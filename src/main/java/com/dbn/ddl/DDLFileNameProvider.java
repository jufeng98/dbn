package com.dbn.ddl;

import com.dbn.object.common.DBObject;
import com.dbn.object.lookup.DBObjectRef;
import com.dbn.object.type.DBObjectType;
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

    public DBObjectType getObjectType() {
        return object.getObjectType();
    }

    public DBObject getObject() {
        return object.get();
    }

    public String getFileName() {
        return toLowerCase(object.getFileName()) + '.' + extension;
    }

    public String getFilePattern() {
        return "*" + toLowerCase(object.getFileName()) + "*." + extension;
    }

}
