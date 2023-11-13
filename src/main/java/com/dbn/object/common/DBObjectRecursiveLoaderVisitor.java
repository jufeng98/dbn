package com.dbn.object.common;

import com.dbn.common.load.ProgressMonitor;
import com.dbn.object.common.list.DBObjectList;
import com.dbn.object.common.list.DBObjectListVisitor;

import java.util.List;

import static com.dbn.common.util.Unsafe.cast;

public class DBObjectRecursiveLoaderVisitor implements DBObjectListVisitor{
    public static final DBObjectRecursiveLoaderVisitor INSTANCE = new DBObjectRecursiveLoaderVisitor();

    private DBObjectRecursiveLoaderVisitor() {
    }

    @Override
    public void visit(DBObjectList<?> objectList) {
        if (!objectList.isMaster()) return;

        List<DBObject> objects = cast(objectList.getObjects());
        for (DBObject object : objects) {
            ProgressMonitor.checkCancelled();
            object.visitChildObjects(this, false);
        }
    }
}
