package com.dbn.object.common.editor;

import com.dbn.common.ui.Presentable;
import com.dbn.object.type.DBObjectType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.dbn.nls.NlsResources.nls;

@Getter
@AllArgsConstructor
public enum DefaultEditorType implements Presentable{
    CODE(nls("app.editor.const.DefaultEditorType_CODE")),
    DATA(nls("app.editor.const.DefaultEditorType_DATA")),
    SPEC(nls("app.editor.const.DefaultEditorType_SPEC")),
    BODY(nls("app.editor.const.DefaultEditorType_BODY")),
    SELECTION(nls("app.editor.const.DefaultEditorType_SELECTION"));

    public static final DefaultEditorType[] EMPTY_ARRAY = new DefaultEditorType[0];

    private final String name;

    public static DefaultEditorType[] getEditorTypes(DBObjectType objectType) {
        switch (objectType){
            case VIEW: return new DefaultEditorType[]{CODE, DATA, SELECTION};
            case PACKAGE: return new DefaultEditorType[]{SPEC, BODY, SELECTION};
            case TYPE: return new DefaultEditorType[]{SPEC, BODY, SELECTION};
        }
        return EMPTY_ARRAY;
    }

    @Override
    public String toString() {
        return name;
    }
}
