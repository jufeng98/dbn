package com.dbn.editor;

public enum EditorProviderId {
    CODE("0.CODE"),
    CODE_SPEC("0.CODE_SPEC"),
    CODE_BODY("1.CODE_BODY"),
    DATA("1.DATA"),
    DDL0("3.DDL"),
    DDL1("4.DDL"),
    DDL2("5.DDL"),
    CONSOLE("0.CONSOLE"),
    SESSION_BROWSER("0.SESSION_BROWSER"),
    DBN_SQL("2.DBN_SQL");

    EditorProviderId(String id) {
        this.id = id;
    }

    private final String id;

    public String getId() {
        return id;
    }
}
