package com.dci.intellij.dbn.data.editor.ui;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

import static com.dci.intellij.dbn.common.util.Unsafe.cast;

@Getter
@Setter
public class BasicDataEditorComponent extends JTextField implements DataEditorComponent{
    private UserValueHolder userValueHolder;

    @Override
    public JTextField getTextField() {
        return this;
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
    }

    @Override
    public void setEnabled(boolean enabled) {
        setEditable(enabled);
    }

    public <T> UserValueHolder<T> getUserValueHolder() {
        return cast(userValueHolder);
    }

    /********************************************************
     *                    Disposable                        *
     ********************************************************/
    private boolean disposed;

    @Override
    public void disposeInner() {
        userValueHolder = null;
    }
}
