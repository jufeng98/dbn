package com.dbn.object.common.list.loader;

import com.dbn.common.content.DynamicContent;
import com.dbn.common.content.DynamicContentElement;
import com.dbn.common.content.DynamicContentType;
import com.dbn.common.content.loader.DynamicSubcontentCustomLoader;
import com.dbn.common.util.Commons;
import com.dbn.database.common.metadata.DBObjectMetadata;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.list.DBObjectList;
import com.dbn.object.common.list.DBObjectRelation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DBObjectListFromRelationListLoader<
                T extends DynamicContentElement,
                M extends DBObjectMetadata>
        extends DynamicSubcontentCustomLoader<T, M> {

    private DBObjectListFromRelationListLoader(String identifier, @Nullable DynamicContentType parentContentType, @NotNull DynamicContentType contentType) {
        super(identifier, parentContentType, contentType);
    }

    public static <T extends DynamicContentElement, M extends DBObjectMetadata> DBObjectListFromRelationListLoader<T, M> create(
            String identifier, @Nullable DynamicContentType parentContentType,
            @NotNull DynamicContentType contentType) {
        return new DBObjectListFromRelationListLoader<>(identifier, parentContentType, contentType);
    }

    @Override
    public T resolveElement(DynamicContent<T> dynamicContent, DynamicContentElement sourceElement) {
        DBObjectList objectList = (DBObjectList) dynamicContent;
        DBObjectRelation objectRelation = (DBObjectRelation) sourceElement;
        DBObject object = (DBObject) objectList.getParent();

        if (Commons.match(object, objectRelation.getSourceObject())) {
            return (T) objectRelation.getTargetObject();
        }
        if (Commons.match(object, objectRelation.getTargetObject())) {
            return (T) objectRelation.getSourceObject();
        }

        return null;
    }
}
