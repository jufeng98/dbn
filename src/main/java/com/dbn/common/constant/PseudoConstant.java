package com.dbn.common.constant;

import com.dbn.common.util.Unsafe;
import lombok.EqualsAndHashCode;

/**
 * Use this "constant" if the possible values are variable (i.e. cannot be implemented with enum).
 */
@EqualsAndHashCode
public abstract class PseudoConstant<T extends PseudoConstant<T>> implements Constant<T> {

    private final String id;
    private final transient int ordinal;

    protected PseudoConstant(String id) {
        if (id == null) {
            // initialization phase (trigger class load - static definitions)
            this.id = null;
            this.ordinal = 0;
        } else {
            this.id = id.intern();
            this.ordinal = PseudoConstantRegistry.register(Unsafe.cast(this));
        }
    }

    @Override
    public final String id() {
        return id;
    }

	@Override
	public int ordinal() {
		return ordinal;
	}

	@Override
	public final String toString() {
		return id();
	}

    public static <T extends PseudoConstant<T>> T get(Class<T> clazz, String id) {
        return PseudoConstantRegistry.get(clazz, id);
    }

    public static <T extends PseudoConstant<T>> T[] values(Class<T> clazz) {
        return PseudoConstantRegistry.values(clazz);
    }

    public static <T extends PseudoConstant<T>> T[] list(Class<T> clazz, String ids) {
        return PseudoConstantRegistry.list(clazz, ids);
    }

}
