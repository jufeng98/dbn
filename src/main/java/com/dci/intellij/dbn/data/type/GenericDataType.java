package com.dci.intellij.dbn.data.type;

import com.dci.intellij.dbn.common.constant.Constant;
import com.dci.intellij.dbn.common.ui.Presentable;
import org.jetbrains.annotations.NotNull;

public enum GenericDataType implements Presentable, Constant<GenericDataType> {
    LITERAL("Literal"),
    NUMERIC("Numeric"),
    DATE_TIME("Date/Time"),
    CLOB("Character Large Object"),
    BLOB("Byte Large Object"),
    ROWID("Row ID"),
    REF("Ref"),
    FILE("File"),
    BOOLEAN("Boolean"),
    OBJECT("Object"),
    CURSOR("Cursor"),
    TABLE("Table"),
    ARRAY("Array"),
    COLLECTION("Collection"),
    XMLTYPE("XML Type"),
    PROPRIETARY("Proprietary"),
    COMPLEX("Complex"),
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
        return is(BLOB, CLOB, XMLTYPE);
    }


}
