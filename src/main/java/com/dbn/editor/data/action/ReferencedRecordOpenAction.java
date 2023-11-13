package com.dbn.editor.data.action;

import com.dbn.editor.data.filter.DatasetFilterInput;

class ReferencedRecordOpenAction extends AbstractRecordsOpenAction {

    ReferencedRecordOpenAction(DatasetFilterInput filterInput) {
        super("Show referenced " + filterInput.getDataset().getName() + " record", filterInput);
    }
}
