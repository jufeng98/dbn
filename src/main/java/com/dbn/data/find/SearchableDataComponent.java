package com.dbn.data.find;

import com.dbn.common.ui.form.DBNForm;
import com.dbn.data.grid.ui.table.basic.BasicTable;
import org.jetbrains.annotations.NotNull;

public interface SearchableDataComponent extends DBNForm {
    void showSearchHeader();
    void hideSearchHeader();
    void cancelEditActions();
    String getSelectedText();

    @NotNull
    BasicTable<?> getTable();
}
