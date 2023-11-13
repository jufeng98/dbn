package com.dbn.connection.jdbc;

import com.dbn.common.property.PropertyHolder;

public interface Resource extends PropertyHolder<ResourceStatus> {

    ResourceType getResourceType();

    String getResourceId();

    boolean isObsolete();

    void statusChanged(ResourceStatus status);
}
