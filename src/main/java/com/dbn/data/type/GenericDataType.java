package com.dbn.data.type;

import com.dbn.common.constant.Constant;
import com.dbn.common.ui.Presentable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.dbn.nls.NlsResources.nls;

@Getter
@AllArgsConstructor
public enum GenericDataType implements Presentable, Constant<GenericDataType> {
    LITERAL(nls("app.data.const.GenericDataType_LITERAL")),
    NUMERIC(nls("app.data.const.GenericDataType_NUMERIC")),
    DATE_TIME(nls("app.data.const.GenericDataType_DATE_TIME")),
    CLOB(nls("app.data.const.GenericDataType_CLOB")),
    NCLOB(nls("app.data.const.GenericDataType_NCLOB")),
    BLOB(nls("app.data.const.GenericDataType_BLOB")),
    ROWID(nls("app.data.const.GenericDataType_ROWID")),
    REF(nls("app.data.const.GenericDataType_REF")),
    FILE(nls("app.data.const.GenericDataType_FILE")),
    BOOLEAN(nls("app.data.const.GenericDataType_BOOLEAN")),
    OBJECT(nls("app.data.const.GenericDataType_OBJECT")),
    CURSOR(nls("app.data.const.GenericDataType_CURSOR")),
    TABLE(nls("app.data.const.GenericDataType_TABLE")),
    ARRAY(nls("app.data.const.GenericDataType_ARRAY")),
    COLLECTION(nls("app.data.const.GenericDataType_COLLECTION")),
    XMLTYPE(nls("app.data.const.GenericDataType_XMLTYPE")),
    PROPRIETARY(nls("app.data.const.GenericDataType_PROPRIETARY")),
    COMPLEX(nls("app.data.const.GenericDataType_COMPLEX")),
    ;

    private final String name;

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
