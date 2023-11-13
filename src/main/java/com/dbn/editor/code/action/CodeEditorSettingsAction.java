package com.dbn.editor.code.action;

import com.dbn.options.ConfigId;
import com.dbn.options.action.ProjectSettingsOpenAction;

public class CodeEditorSettingsAction extends ProjectSettingsOpenAction {

    public CodeEditorSettingsAction() {
        super(ConfigId.CODE_EDITOR, true);
    }
}
