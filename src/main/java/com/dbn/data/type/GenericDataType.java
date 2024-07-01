package com.dbn.data.type;

import com.dbn.common.constant.Constant;
import com.dbn.common.ui.Presentable;
import org.jetbrains.annotations.NotNull;

import static com.dbn.nls.NlsResources.nls;

public enum GenericDataType implements Presentable, Constant<GenericDataType> {
    LITERAL(nls("app.shared.const.GenericDataType_LITERAL")),
    NUMERIC(nls("app.shared.const.GenericDataType_NUMERIC")),
    DATE_TIME(nls("app.shared.const.GenericDataType_DATE_TIME")),
    CLOB(nls("app.shared.const.GenericDataType_CLOB")),
    NCLOB(nls("app.shared.const.GenericDataType_NCLOB")),
    BLOB(nls("app.shared.const.GenericDataType_BLOB")),
    ROWID(nls("app.shared.const.GenericDataType_ROWID")),
    REF(nls("app.shared.const.GenericDataType_REF")),
    FILE(nls("app.shared.const.GenericDataType_FILE")),
    BOOLEAN(nls("app.shared.const.GenericDataType_BOOLEAN")),
    OBJECT(nls("app.shared.const.GenericDataType_OBJECT")),
    CURSOR(nls("app.shared.const.GenericDataType_CURSOR")),
    TABLE(nls("app.shared.const.GenericDataType_TABLE")),
    ARRAY(nls("app.shared.const.GenericDataType_ARRAY")),
    COLLECTION(nls("app.shared.const.GenericDataType_COLLECTION")),
    XMLTYPE(nls("app.shared.const.GenericDataType_XMLTYPE")),
    PROPRIETARY(nls("app.shared.const.GenericDataType_PROPRIETARY")),
    COMPLEX(nls("app.shared.const.GenericDataType_COMPLEX")),
    ;

    private final String name;

    GenericDataType(String name) {
        this.name = name;
    }
    @Override
    @NotNull
    public String getName() {
        return name;
    }

    public boolean is(GenericDataType... genericDataTypes) {
        for (GenericDataType genericDataType : genericDataTypes) {
            if (this == genericDataType) return true;
        }
        return false;
    }

    public boolean isLOB() {
        return is(BLOB, CLOB, NCLOB, XMLTYPE);
    }


}
