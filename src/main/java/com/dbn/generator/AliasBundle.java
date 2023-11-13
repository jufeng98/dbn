package com.dbn.generator;

import com.dbn.common.util.Naming;
import com.dbn.object.common.DBObject;
import com.dbn.object.lookup.DBObjectRef;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AliasBundle {
    private final Map<DBObjectRef, String> aliases = new HashMap<>();

    public String getAlias(DBObject object) {
        DBObjectRef objectRef = object.ref();
        String alias = aliases.get(objectRef);
        if (alias == null) {
            alias = Naming.createAliasName(object.getName());
            alias = getNextAvailable(alias);
            aliases.put(objectRef, alias);
        }
        return alias;
    }

    private String getNextAvailable(String alias) {
        for (String availableAlias : aliases.values()) {
            if (Objects.equals(alias, availableAlias)) {
                alias = Naming.nextNumberedIdentifier(alias, false);
            }
        }
        return alias;
    }

}
