package com.dci.intellij.dbn.object.common;

import com.dci.intellij.dbn.common.dispose.StatefulDisposable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DBObjectDelegate) {
            DBObjectDelegate that = (DBObjectDelegate) o;

            if (this.isDisposed()) return false;
            if (that.isDisposed()) return false;

            return Objects.equals(this.delegate(), that.delegate());
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (this.isDisposed()) return -1;
        return delegate().hashCode();
    }
}
