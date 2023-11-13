package com.dbn.data.value;

import java.sql.SQLException;

public abstract class LargeObjectValue extends ValueAdapter<String> {
    public abstract String read(int maxSize) throws SQLException;
    public abstract long size() throws SQLException;
    public abstract void release();
}
