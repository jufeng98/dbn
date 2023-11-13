package com.dbn.common.environment;

import com.dbn.common.constant.PseudoConstant;
import com.dbn.common.constant.PseudoConstantConverter;

import java.util.UUID;

public final class EnvironmentTypeId extends PseudoConstant<EnvironmentTypeId> {

    public static final EnvironmentTypeId DEFAULT =     get("default");
    public static final EnvironmentTypeId DEVELOPMENT = get("development");
    public static final EnvironmentTypeId INTEGRATION = get("integration");
    public static final EnvironmentTypeId PRODUCTION =  get("production");
    public static final EnvironmentTypeId OTHER =       get("other");

    public EnvironmentTypeId(String id) {
        super(id);
    }

    public static EnvironmentTypeId get(String id) {
        return PseudoConstant.get(EnvironmentTypeId.class, id);
    }

    public static EnvironmentTypeId create() {
        return EnvironmentTypeId.get(UUID.randomUUID().toString());
    }

    public static class Converter extends PseudoConstantConverter<EnvironmentTypeId> {
        public Converter() {
            super(EnvironmentTypeId.class);
        }
    }
}
