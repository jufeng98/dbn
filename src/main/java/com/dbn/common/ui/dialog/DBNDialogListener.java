package com.dbn.common.ui.dialog;

import java.util.EventListener;

public interface DBNDialogListener extends EventListener {
    enum Action{OPEN, CLOSE};

    void onAction(Action action);
}
