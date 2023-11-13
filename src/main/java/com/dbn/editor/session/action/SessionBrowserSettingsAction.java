package com.dbn.editor.session.action;

import com.dbn.options.ConfigId;
import com.dbn.options.action.ProjectSettingsOpenAction;

public class SessionBrowserSettingsAction extends ProjectSettingsOpenAction {
    public SessionBrowserSettingsAction() {
        super(ConfigId.OPERATIONS, true);
    }
}
