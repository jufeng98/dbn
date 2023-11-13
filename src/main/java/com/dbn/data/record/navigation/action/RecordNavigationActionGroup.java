package com.dbn.data.record.navigation.action;

import com.dbn.editor.data.filter.DatasetFilterInput;
import com.intellij.openapi.actionSystem.DefaultActionGroup;

public class RecordNavigationActionGroup extends DefaultActionGroup{

    public RecordNavigationActionGroup(DatasetFilterInput filterInput) {
        add(new RecordEditorOpenAction(filterInput));
        add(new RecordViewerOpenAction(filterInput));
    }
}
