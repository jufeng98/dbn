package com.dbn.object.common;

import com.dbn.common.compatibility.Compatibility;
import com.dbn.object.lookup.DBObjectRef;
import com.intellij.pom.Navigatable;
import lombok.experimental.Delegate;

class DBObjectDelegateBase implements DBObject {
    protected final DBObjectRef<?> ref;

    public DBObjectDelegateBase(DBObject object) {
        this.ref = DBObjectRef.of(object);
    }

    @Delegate(excludes = Navigatable.class)
    public DBObject delegate() {
        return DBObjectRef.ensure(ref);
    }

    @Override
    @Compatibility
    public boolean canNavigateToSource() {
        return delegate().canNavigateToSource();
    }

    @Override
    @Compatibility
    public boolean canNavigate() {
        return delegate().canNavigate();
    }

    @Override
    @Compatibility
    public void navigate(boolean requestFocus) {
        delegate().navigate(requestFocus);
    }
}
