package com.dbn.connection;

import com.dbn.common.property.Property;
import com.dbn.common.property.PropertyHolderBase;

import java.util.Arrays;

public class ConnectionSelectorOptions extends PropertyHolderBase.IntStore<ConnectionSelectorOptions.Option> {

    @Override
    protected Option[] properties() {
        return Option.VALUES;
    }

    public enum Option implements Property.IntBase {
        SHOW_VIRTUAL_CONNECTIONS,
        SHOW_CREATE_CONNECTION,
        PROMPT_SCHEMA_SELECTION;

        public static final Option[] VALUES = values();

        private final IntMasks masks = new IntMasks(this);

        @Override
        public IntMasks masks() {
            return masks;
        }
    }

    public static ConnectionSelectorOptions options(Option ... options) {
        ConnectionSelectorOptions holder = new ConnectionSelectorOptions();
        Arrays.stream(options).forEach(option -> holder.set(option));
        return holder;
    }

}
