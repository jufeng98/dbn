package com.dbn.object.common.editor;

import com.dbn.common.ui.Presentable;
import com.dbn.object.type.DBObjectType;
import lombok.Getter;

@Getter
public enum DefaultEditorType implements Presentable{
    CODE("Code"),
    DATA("Data"),
    SPEC("Spec"),
    BODY("Body"),
    SELECTION("Last Selection");

    private final String name;

    DefaultEditorType(String name) {
        this.name = name;
    }

    public static DefaultEditorType[] getEditorTypes(DBObjectType objectType) {
        switch (objectType){
            case VIEW: return new DefaultEditorType[]{CODE, DATA, SELECTION};
            case PACKAGE: return new DefaultEditorType[]{SPEC, BODY, SELECTION};
            case TYPE: return new DefaultEditorType[]{SPEC, BODY, SELECTION};
        }
        return new DefaultEditorType[0];
    }

    @Override
    public String toString() {
        return name;
    }
}
