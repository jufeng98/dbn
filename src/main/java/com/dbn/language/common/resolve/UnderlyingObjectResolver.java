package com.dbn.language.common.resolve;

import com.dbn.language.common.psi.IdentifierPsiElement;
import com.dbn.object.common.DBObject;

import java.util.HashMap;
import java.util.Map;

public abstract class UnderlyingObjectResolver {
    public static Map<String, UnderlyingObjectResolver> RESOLVERS = new HashMap<>();
    static {
        // TODO remove this and register as app component in plugin xml
        AliasObjectResolver.getInstance();
        AssignmentObjectResolver.getInstance();
        LocalDeclarationObjectResolver.getInstance();
        SurroundingVirtualObjectResolver.getInstance();
    }

    private final String id;

    public UnderlyingObjectResolver(String id) {
        this.id = id;
        RESOLVERS.put(id, this);

    }

    public static UnderlyingObjectResolver get(String id) {
        return RESOLVERS.get(id);
    }

    public final DBObject resolve(IdentifierPsiElement identifierPsiElement) {
        return resolve(identifierPsiElement, 0);
    }

    protected abstract DBObject resolve(IdentifierPsiElement identifierPsiElement, int recursionCheck);
}
