package com.dbn.data.model.basic;

import com.dbn.common.dispose.Failsafe;
import com.dbn.common.util.Strings;
import com.dbn.data.model.ColumnInfo;
import com.dbn.data.type.DBDataType;
import com.dbn.data.type.GenericDataType;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class BasicColumnInfo implements ColumnInfo {
    protected String name;
    protected int index;
    protected DBDataType dataType;

    public BasicColumnInfo(String name, DBDataType dataType, int index) {
        this.name = Strings.intern(name);
        this.index = index;
        this.dataType = dataType;
    }

    @Override
    @NotNull
    public DBDataType getDataType() {
        return Failsafe.nn(dataType);
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean isSortable() {
        DBDataType dataType = getDataType();
        return dataType.isNative() &&
                dataType.getGenericDataType().is(
                        GenericDataType.LITERAL,
                        GenericDataType.NUMERIC,
                        GenericDataType.DATE_TIME);
    }


}
