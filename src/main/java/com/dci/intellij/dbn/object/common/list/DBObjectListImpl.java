package com.dci.intellij.dbn.object.common.list;

import com.dci.intellij.dbn.browser.DatabaseBrowserManager;
import com.dci.intellij.dbn.browser.model.BrowserTreeEventListener;
import com.dci.intellij.dbn.browser.model.BrowserTreeNode;
import com.dci.intellij.dbn.browser.options.DatabaseBrowserSettings;
import com.dci.intellij.dbn.browser.options.DatabaseBrowserSortingSettings;
import com.dci.intellij.dbn.common.content.DynamicContentBase;
import com.dci.intellij.dbn.common.content.DynamicContentProperty;
import com.dci.intellij.dbn.common.content.DynamicContentType;
import com.dci.intellij.dbn.common.content.GroupedDynamicContent;
import com.dci.intellij.dbn.common.content.dependency.ContentDependencyAdapter;
import com.dci.intellij.dbn.common.content.loader.DynamicContentLoader;
import com.dci.intellij.dbn.common.content.loader.DynamicContentLoaderImpl;
import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.event.ProjectEvents;
import com.dci.intellij.dbn.common.filter.CompoundFilter;
import com.dci.intellij.dbn.common.filter.Filter;
import com.dci.intellij.dbn.common.range.Range;
import com.dci.intellij.dbn.common.ref.WeakRefCache;
import com.dci.intellij.dbn.common.search.SearchAdapter;
import com.dci.intellij.dbn.common.string.StringDeBuilder;
import com.dci.intellij.dbn.common.ui.tree.TreeEventType;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.DatabaseEntity;
import com.dci.intellij.dbn.connection.config.ConnectionFilterSettings;
import com.dci.intellij.dbn.database.common.metadata.DBObjectMetadata;
import com.dci.intellij.dbn.navigation.psi.DBObjectListPsiDirectory;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBObjectBundle;
import com.dci.intellij.dbn.object.common.DBVirtualObject;
import com.dci.intellij.dbn.object.common.property.DBObjectProperty;
import com.dci.intellij.dbn.object.common.sorting.DBObjectComparator;
import com.dci.intellij.dbn.object.common.sorting.DBObjectComparators;
import com.dci.intellij.dbn.object.common.sorting.SortingType;
import com.dci.intellij.dbn.object.filter.quick.ObjectQuickFilter;
import com.dci.intellij.dbn.object.filter.quick.ObjectQuickFilterManager;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.dci.intellij.dbn.object.type.DBObjectType;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import java.util.*;
import java.util.function.Consumer;

import static com.dci.intellij.dbn.common.content.DynamicContentProperty.*;
import static com.dci.intellij.dbn.common.dispose.Checks.isValid;
import static com.dci.intellij.dbn.common.dispose.Failsafe.guarded;
import static com.dci.intellij.dbn.common.list.FilteredList.unwrap;
import static com.dci.intellij.dbn.common.search.Search.binarySearch;
import static com.dci.intellij.dbn.common.search.Search.comboSearch;
import static com.dci.intellij.dbn.common.util.Commons.nvl;
import static com.dci.intellij.dbn.object.common.DBObjectSearchAdapters.binary;
import static com.dci.intellij.dbn.object.common.DBObjectSearchAdapters.linear;
import static com.dci.intellij.dbn.object.common.sorting.DBObjectComparators.*;
import static com.dci.intellij.dbn.object.type.DBObjectType.*;
import static java.util.Collections.emptyList;

@Slf4j
@Getter
@Setter
public class DBObjectListImpl<T extends DBObject> extends DynamicContentBase<T> implements DBObjectList<T> {
    private static final WeakRefCache<DBObjectList, ObjectQuickFilter> quickFilterCache = WeakRefCache.weakKey();

    private final DBObjectType objectType;

    DBObjectListImpl(
            @NotNull DBObjectType objectType,
            @NotNull DatabaseEntity parent,
            ContentDependencyAdapter dependencyAdapter,
            DynamicContentProperty... properties) {
        super(parent, dependencyAdapter, properties);
        this.objectType = objectType;
        if ((parent instanceof DBSchema || parent instanceof DBObjectBundle) && !isInternal()) {
            ObjectQuickFilterManager quickFilterManager = ObjectQuickFilterManager.getInstance(getProject());
            quickFilterManager.restoreQuickFilter(this);
        }
    }

    @Override
    public DynamicContentLoader<T, DBObjectMetadata> getLoader() {
        DatabaseEntity parent = getParent();
        if (parent instanceof DBVirtualObject) {
            return DynamicContentLoader.VOID_CONTENT_LOADER;
        } else {
            DynamicContentType parentContentType = parent.getDynamicContentType();
            return DynamicContentLoaderImpl.resolve(parentContentType, objectType);
        }
    }

    @Nullable
    public static <E extends DBObject> List<E> getObjects(@Nullable DBObjectList<E> objectList) {
        return objectList == null ? null : objectList.getObjects();

    }

    @Nullable
    public static <E extends DBObject> E getObject(@Nullable DBObjectList<E> objectList, String name) {
        return getObject(objectList, name, (short) 0);
    }

    public static <E extends DBObject> E getObject(@Nullable DBObjectList<E> objectList, String name, short overload) {
        return objectList == null ? null : objectList.getObject(name, overload);
    }

    @Nullable
    @Override
    public Filter<T> getFilter() {
        Filter<T> configFilter = getConfigFilter();
        ObjectQuickFilter<T> quickFilter = getQuickFilter();

        if (configFilter != null && quickFilter != null) return CompoundFilter.of(configFilter, quickFilter);
        if (configFilter != null) return configFilter;
        return quickFilter;
    }

    @Nullable
    @Override
    public ObjectQuickFilter<T> getQuickFilter() {
        return quickFilterCache.get(this);
    }

    @Override
    public void setQuickFilter(@Nullable ObjectQuickFilter<T> quickFilter) {
        quickFilterCache.set(this, quickFilter);

    }

    @Override
    @Nullable
    public Filter<T> getConfigFilter() {
        ConnectionHandler connection = this.getConnection();
        if (isValid(connection) && !connection.isVirtual()) {
            ConnectionFilterSettings filterSettings = connection.getSettings().getFilterSettings();
            return filterSettings.getNameFilter(objectType);
        }
        return null;
    }

    @Override
    @NotNull
    public List<T> getObjects() {
        return getAllElements();
    }

    @NotNull
    @Override
    public String getQualifiedName() {
        StringDeBuilder builder = new StringDeBuilder();
        builder.append(getName());

        DatabaseEntity parent = getParent();
        while(parent != null) {
            builder.prepend('.');
            builder.prepend(parent.getName());
            if (parent instanceof DBObject) {
                DBObject object = (DBObject) parent;
                parent = object.getParent();
            } else {
                parent = parent.getParentEntity();
            }
        }
        return builder.toString();
    }

    @Override
    public void collectObjects(Consumer<? super DBObject> consumer) {
        for (T object : getAllElements()) {
            consumer.accept(object);
        }
    }

    @Override
    public List<T> getObjects(String name) {
        return getElements(name);
    }

    @Override
    public void addObject(T object) {
        if (elements == EMPTY_CONTENT || elements == EMPTY_UNTOUCHED_CONTENT) {
            elements = new ArrayList<>();
        }

        if (!elements.contains(object)) {
            elements.add(object);
        }
    }

    @Override
    public T getObject(String name) {
        return getElement(name, (short) 0);
    }

    @Override
    public T getObject(String name, short overload) {
        return getElement(name, overload);
    }

    @Override
    public boolean contains(T object) {
        return elements.contains(object);
    }

    @Override
    public T getElement(String name, short overload) {
        if (name == null) return null;

        List<T> elements = getAllElements();
        if (elements.isEmpty()) return null;

        if (objectType == ARGUMENT || objectType == TYPE_ATTRIBUTE) {
            // arguments and type attributes are sorted by position (linear search)
            return super.getElement(name, overload);
        }

        if (objectType == TYPE) {
            T element = binarySearch(elements, binary(name, overload, false));
            if (element == null) element = binarySearch(elements, binary(name, overload, true));
            return element;
        }

        if (isSearchable()) {
            if (objectType == COLUMN) {
                // primary key columns are sorted by position at beginning of the list of elements
                SearchAdapter<T> linear = linear(name, c -> c instanceof DBColumn && ((DBColumn) c).isPrimaryKey());
                SearchAdapter<T> binary = binary(name);
                return comboSearch(elements, linear, binary);
            }  else {
                SearchAdapter<T> binary = objectType.isOverloadable() ?
                        binary(name, overload) :
                        binary(name);

                return binarySearch(elements, binary);

            }
        }

        return super.getElement(name, overload);
    }


    @Override
    public boolean isInternal() {
        return is(INTERNAL);
    }

    @Override
    public boolean isHidden() {
        return is(HIDDEN);
    }

    @Override
    public boolean isDependency() {
        return is(DEPENDENCY);
    }

    private boolean isSearchable() {
        return is(SEARCHABLE);
    }

    @Override
    protected void sortElements(List<T> elements) {
        if (is(VIRTUAL)) {
            elements.sort(classic());

        } else if (isInternal()) {
            if (is(GROUPED)) {
                elements.sort(generic());
            } else {
                elements.sort(DBObjectComparators.basic(objectType));
                set(SEARCHABLE, true);
            }
        } else {
            DBObjectComparator<T> comparator = classic();
            if (objectType == TYPE) {
                comparator = detailed(objectType, DBObjectProperty.COLLECTION, SortingType.NAME);
            } else if (objectType != ANY) {
                DatabaseBrowserSettings browserSettings = DatabaseBrowserSettings.getInstance(getProject());
                DatabaseBrowserSortingSettings sortingSettings = browserSettings.getSortingSettings();
                comparator = nvl(sortingSettings.getComparator(objectType), comparator);
            }

            elements.sort(comparator);
            boolean searchable = comparator.getSortingType() == SortingType.NAME;
            set(SEARCHABLE, searchable);
        }
    }

    @Override
    @NotNull
    public String getName() {
        return objectType.getListName();
    }

    @Override
    public void initTreeElement() {
        if (!isLoading() && !isLoaded()) {
            getObjects();
        }
    }

    @Override
    @NotNull
    public Project getProject() {
        DatabaseEntity parent = getParentEntity();
        return Failsafe.nn(parent.getProject());
    }

    @Override
    public PsiDirectory getPsiDirectory() {
        return DBObjectListPsiDirectory.of(this);
    }

    @Override
    public void notifyChangeListeners() {
        guarded(this, n -> {
            Project project = n.getProject();
            BrowserTreeNode treeParent = n.getParent();
            if (!n.isInternal() && n.isTouched() && isValid(project) && treeParent.isTreeStructureLoaded()) {
                ProjectEvents.notify(project,
                        BrowserTreeEventListener.TOPIC,
                        l -> l.nodeChanged(n, TreeEventType.STRUCTURE_CHANGED));
            }
        });
    }

    /*********************************************************
     *                   LoadableContent                     *
     *********************************************************/
    @Override
    public String getContentDescription() {
        if (isDisposed()) {
            return "disposed";
        } else {
            if (getParent() instanceof DBObject) {
                DBObject object = (DBObject) getParent();
                return getName() + " of " + object.getQualifiedNameWithType();
            }
            ConnectionHandler connection = this.getConnection();
            return getName() + " from " + connection.getName();
        }
    }

    /*********************************************************
     *                     TreeElement                       *
     *********************************************************/

    @Override
    public boolean isTreeStructureLoaded() {
        return isTouched();
    }

    public boolean isTouched() {
        return elements != EMPTY_UNTOUCHED_CONTENT;
    }

    @Override
    public boolean canExpand() {
        return isTouched() && getChildCount() > 0;
    }

    @Override
    public int getTreeDepth() {
        BrowserTreeNode treeParent = getParent();
        return treeParent.getTreeDepth() + 1;
    }

    @Override
    public BrowserTreeNode getChildAt(int index) {
        return getChildren().get(index);
    }

    @Override
    @NotNull
    public BrowserTreeNode getParent() {
        return getParentEntity();
    }

    @Override
    public int getIndex(TreeNode node) {
        return getIndex((BrowserTreeNode) node);
    }

    @Override
    public boolean getAllowsChildren() {
        return !isLeaf();
    }

    @Override
    public Enumeration<? extends BrowserTreeNode> children() {
        return Collections.enumeration(getChildren());
    }

    @Override
    public List<? extends BrowserTreeNode> getChildren() {
        return guarded(elements, this, l -> {
            boolean wasUntouched = !l.isTouched();
            l.getElements();
            if (wasUntouched && l.isLoaded()) {
                ConnectionHandler connection = l.getConnection();
                DatabaseBrowserManager.scrollToSelectedElement(connection);
            }
            return l.elements;
        });
    }

    @Override
    public void refreshTreeChildren(@NotNull DBObjectType... objectTypes) {
        if (isLoaded()) {
            if (objectType.isOneOf(objectTypes)) {
                notifyChangeListeners();
            }

            for (DBObject object : getObjects()) {
                object.refreshTreeChildren(objectTypes);
            }
        }
    }

    @Override
    public void rebuildTreeChildren() {
        if (isLoaded()) {
            for (DBObject object : getObjects()) {
                object.rebuildTreeChildren();
            }
        }
    }

    @Override
    public int getChildCount() {
        return getChildren().size();
    }

    @Override
    public boolean isLeaf() {
        return getChildren().isEmpty();
    }

    @Override
    public int getIndex(BrowserTreeNode child) {
        return getChildren().indexOf(child);
    }

    @Override
    public DynamicContentType getContentType() {
        return objectType;
    }

    @Override
    public Icon getIcon(int flags) {
        return objectType.getListIcon();
    }

    @Override
    public String getPresentableText() {
        return objectType.getPresentableListName();
    }

    @Override
    public String getPresentableTextDetails() {
        int elementCount = getChildCount();
        int unfilteredElementCount = unwrap(elements).size();
        return unfilteredElementCount > 0 ? "(" + elementCount + (elementCount != unfilteredElementCount ? "/"+ unfilteredElementCount : "") + ")" : null;
    }

    @Override
    public String getPresentableTextConditionalDetails() {
        return null;
    }

    /*********************************************************
    *                    ToolTipProvider                    *
    *********************************************************/
    @Override
    public String getToolTip() {
        return null;
    }

    /*********************************************************
     *                  NavigationItem                       *
     *********************************************************/
    @Override
    public void navigate(boolean requestFocus) {
        DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(getProject());
        browserManager.navigateToElement(this, requestFocus, true);
    }

    @Override
    public boolean canNavigate() {
        return false;
    }

    @Override
    public boolean canNavigateToSource() {
        return false;
    }

    @Override
    public ItemPresentation getPresentation() {
        return this;
    }

    /*********************************************************
     *                 ItemPresentation                      *
     *********************************************************/
    @Override
    public String getLocationString() {
        return null;
    }

    @Override
    public Icon getIcon(boolean open) {
        return getIcon(0);
    }

    public String toString() {
        if (isDisposed()) {
            return getName() + " - " + super.toString();
        }

        /*if (getTreeParent() instanceof DBObject) {
            DBObject object = (DBObject) getTreeParent();
            return getName() + " of " + object.getQualifiedNameWithType();
        }*/
        DatabaseEntity parentEntity = getParentEntity();
        return parentEntity.getDynamicContentType() + " (" + parentEntity.getName() + ") " + getName() + " - " + super.toString();
    }

    @Override
    public int compareTo(@NotNull DBObjectList objectList) {
        return objectType.compareTo(objectList.getObjectType());
    }

    @Override
    public void disposeInner() {
        super.disposeInner();
        quickFilterCache.remove(this);
        changeSignature();
    }

    public static class Grouped<T extends DBObject> extends DBObjectListImpl<T> implements GroupedDynamicContent<T> {
        private Map<DBObjectRef, Range> ranges;

        Grouped(
                @NotNull DBObjectType objectType,
                @NotNull DatabaseEntity parent,
                ContentDependencyAdapter dependencyAdapter,
                DynamicContentProperty... statuses) {
            super(objectType, parent, dependencyAdapter, statuses);
            set(GROUPED, true);
        }


        @Override
        protected void afterUpdate() {
            List<T> elements = unwrap(this.elements);
            if (elements.isEmpty()) return;

            Map<DBObjectRef, Range> ranges = new HashMap<>();

            DBObjectRef currentParent = null;
            int rangeStart = 0;
            for (int i = 0; i < elements.size(); i++) {
                T object = elements.get(i);
                DBObjectRef parent = object.getParentObject().ref();
                currentParent = nvl(currentParent, parent);

                if (!Objects.equals(currentParent, parent)) {
                    ranges.put(currentParent, new Range(rangeStart, i - 1));
                    currentParent = parent;
                    rangeStart = i;
                }

                if (i == elements.size() - 1) {
                    ranges.put(currentParent, new Range(rangeStart, i));
                }
            }

            this.ranges = ranges;
        }

        public List<T> getChildElements(DatabaseEntity entity) {
            // "touch" elements first for ranges to become available (fragile...)
            List<T> elements = getAllElements();
            val ranges = this.ranges;
            if (ranges == null) return emptyList();
            if (!entity.isObject()) return emptyList();

            DBObject object = (DBObject) entity;
            Range range = ranges.get(object.ref());
            if (range == null) return emptyList();

            int size = elements.size();
            if (size == 0) return emptyList();

            int fromIndex = range.getLeft();
            int toIndex = range.getRight() + 1;
            if (toIndex > size) {
                log.error("invalid range {} for elements size {}", range, elements.size(),
                        new IllegalArgumentException("Invalid range capture"));
                toIndex = size;
            }
            return elements.subList(fromIndex, toIndex);
        }

        @Override
        public T getElement(String name, short overload) {
            // "touch" elements first for ranges to become available (fragile...)
            getElements();
            if (ranges == null) return null;

            SearchAdapter<T> adapter = getObjectType().isOverloadable() ?
                    binary(name, overload) :
                    binary(name);
            Collection<Range> ranges = this.ranges.values();
            for (Range range : ranges) {
                T element = binarySearch(elements, range.getLeft(), range.getRight(), adapter);
                if (element != null) {
                    return element;
                }
            }
            return null;
        }
    }
}
