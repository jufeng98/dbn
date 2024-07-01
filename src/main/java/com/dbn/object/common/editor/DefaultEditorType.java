package com.dbn.object.common.editor;

import com.dbn.common.ui.Presentable;
import com.dbn.object.type.DBObjectType;
import lombok.Getter;

import static com.dbn.nls.NlsResources.nls;

@Getter
public enum DefaultEditorType implements Presentable{
    CODE(nls("app.objectEditor.const.DefaultEditorType_CODE")),
    DATA(nls("app.objectEditor.const.DefaultEditorType_DATA")),
    SPEC(nls("app.objectEditor.const.DefaultEditorType_SPEC")),
    BODY(nls("app.objectEditor.const.DefaultEditorType_BODY")),
    SELECTION(nls("app.objectEditor.const.DefaultEditorType_SELECTION"));

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
