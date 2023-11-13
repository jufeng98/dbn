package com.dbn.object.common.list;

import com.dbn.common.lookup.Visitor;

@FunctionalInterface
public interface DBObjectListVisitor extends Visitor<DBObjectList<?>> {
    void visit(DBObjectList<?> objectList);
}
