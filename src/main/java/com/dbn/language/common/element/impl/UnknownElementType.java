package com.dbn.language.common.element.impl;

import com.dbn.language.common.element.ElementTypeBundle;
import com.dbn.language.common.element.cache.ElementTypeLookupCache;
import com.dbn.language.common.element.cache.VoidElementTypeLookupCache;

public final class UnknownElementType extends BasicElementType{
    public UnknownElementType(ElementTypeBundle bundle) {
        super(bundle, "UNKNOWN", "Unidentified element type.");
    }

    @Override
    public ElementTypeLookupCache createLookupCache() {
        return new VoidElementTypeLookupCache<>(this);
    }
}
