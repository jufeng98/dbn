package com.dci.intellij.dbn.object.common;

import com.dci.intellij.dbn.common.dispose.StatefulDisposableBase;
import com.dci.intellij.dbn.common.load.ProgressMonitor;
import com.dci.intellij.dbn.object.common.list.DBObjectList;
import com.dci.intellij.dbn.object.common.list.DBObjectListVisitor;

import java.util.List;

import static com.dci.intellij.dbn.common.util.Unsafe.cast;

public class DBObjectRecursiveLoaderVisitor extends StatefulDisposableBase implements DBObjectListVisitor{
    public static final DBObjectRecursiveLoaderVisitor INSTANCE = new DBObjectRecursiveLoaderVisitor();

    private DBObjectRecursiveLoaderVisitor() {
    }

    @Override
    public void visit(DBObjectList<?> objectList) {
        if (!objectList.isMaster()) return;

        List<DBObject> objects = cast(objectList.getObjects());
        for (DBObject object : objects) {
            checkDisposed();
            ProgressMonitor.checkCancelled();
            object.visitChildObjects(this, false);
        }
    }
}
