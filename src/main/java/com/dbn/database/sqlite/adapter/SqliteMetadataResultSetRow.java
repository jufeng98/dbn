package com.dbn.database.sqlite.adapter;

public interface SqliteMetadataResultSetRow<T extends SqliteMetadataResultSetRow> extends Comparable<T> {
    String identifier();

    @Override
    default int compareTo( T o) {
        return identifier().compareTo(o.identifier());
    }
}
