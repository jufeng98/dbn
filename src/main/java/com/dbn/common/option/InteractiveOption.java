package com.dbn.common.option;

import com.dbn.common.ui.Presentable;

public interface InteractiveOption extends Presentable{
    boolean isCancel();

    boolean isAsk();
}
