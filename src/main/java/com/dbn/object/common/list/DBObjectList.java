package com.dbn.object.common.list;

import com.dbn.browser.model.BrowserTreeNode;
import com.dbn.common.content.DynamicContent;
import com.dbn.common.filter.Filter;
import com.dbn.object.common.DBObject;
import com.dbn.object.filter.quick.ObjectQuickFilter;
import com.dbn.object.type.DBObjectType;
import com.intellij.psi.PsiDirectory;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public interface DBObjectList<T extends DBObject> extends BrowserTreeNode, DynamicContent<T>, Comparable<DBObjectList> {

    PsiDirectory getPsiDirectory();

    DBObjectType getObjectType();

    void addObject(T object);

    boolean isInternal();

    boolean isHidden();

    boolean isDependency();

    @Nullable
    Filter<T> getConfigFilter();

    @Nullable
    ObjectQuickFilter<T> getQuickFilter();

    void setQuickFilter(@Nullable ObjectQuickFilter<T> quickFilter);

    List<T> getObjects();

    List<T> getObjects(String name);

    T getObject(String name);

    T getObject(String name, short overload);

    void collectObjects(Consumer<? super DBObject> consumer);

    boolean contains(T object);
}
