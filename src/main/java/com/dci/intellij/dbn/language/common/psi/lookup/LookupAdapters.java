package com.dci.intellij.dbn.language.common.psi.lookup;

import com.dci.intellij.dbn.language.common.element.util.IdentifierCategory;
import com.dci.intellij.dbn.language.common.element.util.IdentifierType;
import com.dci.intellij.dbn.language.common.psi.IdentifierPsiElement;
import com.dci.intellij.dbn.object.type.DBObjectType;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class LookupAdapters {
    private static final Map<DBObjectType, PsiLookupAdapter> OBJECT = new ConcurrentHashMap<>();
    private static final Map<DBObjectType, PsiLookupAdapter> OBJECT_DEFINITION = new ConcurrentHashMap<>();
    private static final Map<DBObjectType, PsiLookupAdapter> ALIAS_DEFINITION = new ConcurrentHashMap<>();
    private static final Map<DBObjectType, PsiLookupAdapter> ALIAS_REFERENCE = new ConcurrentHashMap<>();
    private static final Map<DBObjectType, PsiLookupAdapter> IDENTIFIER_DEFINITION = new ConcurrentHashMap<>();
    private static final Map<DBObjectType, PsiLookupAdapter> IDENTIFIER_REFERENCE = new ConcurrentHashMap<>();
    private static final Map<DBObjectType, PsiLookupAdapter> VARIABLE_DEFINITION = new ConcurrentHashMap<>();
    private static final Map<DBObjectType, Map<DBObjectType, PsiLookupAdapter>> VIRTUAL_OBJECT = new ConcurrentHashMap<>();

    public static PsiLookupAdapter identifierDefinition(DBObjectType objectType) {
        return IDENTIFIER_DEFINITION.computeIfAbsent(objectType, t -> new IdentifierLookupAdapter(null, null, IdentifierCategory.DEFINITION, objectType, null));
    }

    public static PsiLookupAdapter identifierReference(DBObjectType objectType) {
        return IDENTIFIER_REFERENCE.computeIfAbsent(objectType, t -> new IdentifierLookupAdapter(null, null, IdentifierCategory.REFERENCE, objectType, null));
    }

    public static PsiLookupAdapter object(DBObjectType objectType)  {
        return OBJECT.computeIfAbsent(objectType, t -> new ObjectLookupAdapter(null, objectType));
    }

    public static PsiLookupAdapter virtualObject(DBObjectType parent, DBObjectType child)  {
        return VIRTUAL_OBJECT.computeIfAbsent(parent, p -> new ConcurrentHashMap<>()).computeIfAbsent(child, c -> new VirtualObjectLookupAdapter(parent, child));
    }

    public static PsiLookupAdapter aliasDefinition(DBObjectType objectType) {
        return ALIAS_DEFINITION.computeIfAbsent(objectType, t -> new IdentifierLookupAdapter(null, IdentifierType.ALIAS, IdentifierCategory.DEFINITION, objectType, null));
    }

    public static PsiLookupAdapter aliasReference(DBObjectType objectType)  {
        return ALIAS_REFERENCE.computeIfAbsent(objectType, t -> new IdentifierLookupAdapter(null, IdentifierType.ALIAS, IdentifierCategory.REFERENCE, objectType, null));
    }

    public static PsiLookupAdapter objectDefinition(DBObjectType objectType) {
        return OBJECT_DEFINITION.computeIfAbsent(objectType, t -> new ObjectDefinitionLookupAdapter(null, t, null));
    }

    public static PsiLookupAdapter variableDefinition(DBObjectType objectType) {
        return VARIABLE_DEFINITION.computeIfAbsent(objectType, t -> new VariableDefinitionLookupAdapter(null, t, null));
    }

    public static PsiLookupAdapter aliasDefinition(IdentifierPsiElement lookupIssuer, DBObjectType objectType, CharSequence identifierName) {
        return new IdentifierLookupAdapter(lookupIssuer, IdentifierType.ALIAS, IdentifierCategory.DEFINITION, objectType, identifierName);
    }

    public static PsiLookupAdapter variableDefinition(IdentifierPsiElement lookupIssuer, DBObjectType objectType, CharSequence identifierName) {
        return new VariableDefinitionLookupAdapter(lookupIssuer, objectType, identifierName);
    }
}
