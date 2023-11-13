package com.dbn.connection.jdbc;

import com.dbn.common.property.PropertyHolderBase;

class ResourceStatusHolder extends PropertyHolderBase.IntStore<ResourceStatus> {

    @Override
    protected ResourceStatus[] properties() {
        return ResourceStatus.VALUES;
    }
}
