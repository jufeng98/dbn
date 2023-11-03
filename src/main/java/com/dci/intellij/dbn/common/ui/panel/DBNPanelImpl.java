package com.dci.intellij.dbn.common.ui.panel;

import com.intellij.ui.components.JBPanel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DBNPanelImpl extends JBPanel implements DBNPanel{
    private boolean disposed;

    @Override
    public void disposeInner() {
        nullify();
    }
}
