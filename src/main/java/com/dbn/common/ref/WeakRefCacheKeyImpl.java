package com.dbn.common.ref;

import com.intellij.util.containers.ContainerUtil;

import java.util.Map;

class WeakRefCacheKeyImpl<K, V> extends WeakRefCacheBase<K, V> {

    @Override
    protected Map<K, V> createCache() {
        return ContainerUtil.createConcurrentWeakMap();
    }
}
