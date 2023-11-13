package com.dbn.object.common.property;

import com.dbn.common.property.PropertyHolderBase;

public class DBObjectProperties extends PropertyHolderBase.LongStore<DBObjectProperty> {

    @Override
    protected DBObjectProperty[] properties() {
        return DBObjectProperty.VALUES;
    }
}
