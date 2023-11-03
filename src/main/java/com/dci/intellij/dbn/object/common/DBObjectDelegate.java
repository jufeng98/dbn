package com.dci.intellij.dbn.object.common;

import com.dci.intellij.dbn.common.dispose.StatefulDisposable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class DBObjectDelegate extends DBObjectDelegateBase implements StatefulDisposable {

    private boolean disposed;

    public DBObjectDelegate(DBObject object) {
        super(object);
    }

    @Override
    public void dispose() {
        // do not dispose the delegated object
        disposed = true;
    }

    @Override
    public void disposeInner() {
        // do not dispose the delegated object
        disposed = true;
    }
}
