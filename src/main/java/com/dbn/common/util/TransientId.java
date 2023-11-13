package com.dbn.common.util;

import com.dbn.common.constant.PseudoConstant;

import java.util.UUID;

public final class TransientId extends PseudoConstant<TransientId> {

    private TransientId(String id) {
        super(id);
    }

    public static TransientId get(String id) {
        return PseudoConstant.get(TransientId.class, id);
    }

    public static TransientId create() {
        return new TransientId(UUID.randomUUID().toString());
    }
}
