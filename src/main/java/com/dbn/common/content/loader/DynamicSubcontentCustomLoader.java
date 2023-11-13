package com.dbn.common.content.loader;

import com.dbn.common.content.*;
import com.dbn.common.content.dependency.ContentDependencyAdapter;
import com.dbn.common.content.dependency.SubcontentDependencyAdapter;
import com.dbn.common.content.*;
import com.dbn.connection.DatabaseEntity;
import com.dbn.database.common.metadata.DBObjectMetadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class DynamicSubcontentCustomLoader<
                T extends DynamicContentElement,
                M extends DBObjectMetadata>
        extends DynamicContentLoaderImpl<T, M>
        implements DynamicContentLoader<T, M> {

    public DynamicSubcontentCustomLoader(
            String identifier, @Nullable DynamicContentType parentContentType,
            @NotNull DynamicContentType contentType) {

        super(identifier, parentContentType, contentType, true);
    }

    protected abstract T resolveElement(DynamicContent<T> dynamicContent, DynamicContentElement sourceElement);

    @Override
    public void loadContent(DynamicContent<T> content) {
        List<T> list = null;
        ContentDependencyAdapter adapter = content.getDependencyAdapter();
        if (adapter instanceof SubcontentDependencyAdapter) {
            SubcontentDependencyAdapter dependencyAdapter = (SubcontentDependencyAdapter) adapter;
            DynamicContent sourceContent = dependencyAdapter.getSourceContent();
            if (sourceContent instanceof GroupedDynamicContent) {
                GroupedDynamicContent groupedContent = (GroupedDynamicContent) sourceContent;
                DatabaseEntity parentEntity = content.ensureParentEntity();
                List<DynamicContentElement> childElements = groupedContent.getChildElements(parentEntity);
                list = childElements.stream().map(e -> resolveElement(content, e)).filter(e -> e != null).collect(Collectors.toList());
            } else {
                List elements = sourceContent.getAllElements();
                for (Object object : elements) {
                    content.checkDisposed();
                    DynamicContentElement sourceElement = (DynamicContentElement) object;
                    T element = resolveElement(content, sourceElement);
                    if (element != null) {
                        content.checkDisposed();
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        list.add(element);
                    }
                }

            }
        }

        content.setElements(list);
        content.set(DynamicContentProperty.MASTER, false);
    }
}
