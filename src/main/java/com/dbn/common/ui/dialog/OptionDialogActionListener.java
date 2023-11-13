package com.dbn.common.ui.dialog;

import com.dbn.common.ui.Presentable;

import java.util.EventListener;

public interface OptionDialogActionListener<O extends Presentable> extends EventListener {
    void actionPerformed(int actionIndex, O selectedOption);
}
