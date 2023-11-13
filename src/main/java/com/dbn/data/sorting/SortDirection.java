package com.dbn.data.sorting;

public enum SortDirection {
    INDEFINITE(0, ""),
    ASCENDING(1, "asc"),
    DESCENDING(-1, "desc");

    private final int compareAdj;
    private final String sqlToken;

    SortDirection(int compareAdj, String sqlToken) {
        this.compareAdj = compareAdj;
        this.sqlToken = sqlToken;
    }

    public int getCompareAdj() {
        return compareAdj;
    }

    public boolean isIndefinite() {
        return this == INDEFINITE;
    }

    public String getSqlToken() {
        return sqlToken;
    }
}
