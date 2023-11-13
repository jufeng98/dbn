package com.dbn.connection.jdbc;

import com.dbn.common.property.Property;
import lombok.Getter;

@Getter
public enum ResourceStatus implements Property.IntBase {
    ACTIVE,
    VALID,
    CLOSED,
    CANCELLED,
    RESERVED,
    READ_ONLY,
    AUTO_COMMIT,

    EVALUATING_VALID(true),
    EVALUATING_CLOSED(true),
    EVALUATING_CANCELLED(true),
    EVALUATING_READ_ONLY(true),
    EVALUATING_AUTO_COMMIT(true),

    CHANGING_VALID(true),
    CHANGING_CLOSED(true),
    CHANGING_CANCELLED(true),
    CHANGING_READ_ONLY(true),
    CHANGING_AUTO_COMMIT(true),

    // transient statuses
    CLOSING(true),
    CANCELLING(true),
    COMMITTING(true),
    ROLLING_BACK(true),

    CREATING_SAVEPOINT(true),
    COMMITTING_SAVEPOINT(true),
    ROLLING_BACK_SAVEPOINT(true),
    RELEASING_SAVEPOINT(true),

    RESOLVING_TRANSACTION(true);

    public static final ResourceStatus[] VALUES = values();

    private final boolean transitory;
    private final IntMasks masks = new IntMasks(this);

    ResourceStatus() {
        this(false);
    }

    ResourceStatus(boolean transitory) {
        this.transitory = transitory;
    }

    @Override
    public IntMasks masks() {
        return masks;
    }
}
