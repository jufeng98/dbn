package com.dci.intellij.dbn.language.common.element.impl;

import com.dci.intellij.dbn.code.common.style.formatting.FormattingDefinition;
import com.dci.intellij.dbn.code.common.style.formatting.FormattingDefinitionFactory;
import com.dci.intellij.dbn.code.common.style.formatting.IndentDefinition;
import com.dci.intellij.dbn.code.common.style.formatting.SpacingDefinition;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.language.common.DBLanguage;
import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.language.common.TokenType;
import com.dci.intellij.dbn.language.common.element.ElementType;
import com.dci.intellij.dbn.language.common.element.ElementTypeBundle;
import com.dci.intellij.dbn.language.common.element.TokenPairTemplate;
import com.dci.intellij.dbn.language.common.element.cache.ElementTypeLookupCache;
import com.dci.intellij.dbn.language.common.element.parser.Branch;
import com.dci.intellij.dbn.language.common.element.parser.BranchCheck;
import com.dci.intellij.dbn.language.common.element.parser.ElementTypeParser;
import com.dci.intellij.dbn.language.common.element.path.LanguageNode;
import com.dci.intellij.dbn.language.common.element.util.ElementTypeAttribute;
import com.dci.intellij.dbn.language.common.element.util.ElementTypeAttributeHolder;
import com.dci.intellij.dbn.language.common.element.util.ElementTypeDefinitionException;
import com.dci.intellij.dbn.object.type.DBObjectType;
import com.intellij.psi.tree.IElementType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;

import static com.dci.intellij.dbn.common.options.setting.Settings.stringAttribute;

@Slf4j
@Getter
@Setter
public abstract class ElementTypeBase extends IElementType implements ElementType {
    private static final FormattingDefinition STATEMENT_FORMATTING = new FormattingDefinition(null, IndentDefinition.NORMAL, SpacingDefinition.MIN_LINE_BREAK, null);

    private final String id;
    private final int hashCode;
    private String description;
    private Icon icon;
    private Branch branch;
    private FormattingDefinition formatting;

    private final ElementTypeLookupCache<?> lookupCache = createLookupCache();
    private final ElementTypeParser parser = createParser();
    private final ElementTypeBundle bundle;
    private final ElementType parent;
    private DBObjectType virtualObjectType;
    private ElementTypeAttributeHolder attributes;

    protected WrappingDefinition wrapping;

    private boolean scopeDemarcation;
    private boolean scopeIsolation;

    ElementTypeBase(@NotNull ElementTypeBundle bundle, ElementType parent, String id, @Nullable String description) {
        super(id, bundle.getLanguageDialect(), false);
        this.id = id.intern();
        this.hashCode = System.identityHashCode(this);
        this.description = description;
        this.bundle = bundle;
        this.parent = parent;
    }

    ElementTypeBase(@NotNull ElementTypeBundle bundle, ElementType parent, String id, @NotNull Element def) throws ElementTypeDefinitionException {
        super(id, bundle.getLanguageDialect(), false);
        String defId = stringAttribute(def, "id");
        this.hashCode = System.identityHashCode(this);
        if (!Objects.equals(id, defId)) {
            defId = id;
            def.setAttribute("id", defId);
            bundle.markIdsDirty();
        }
        this.id = defId.intern();
        this.bundle = bundle;
        this.parent = parent;
        if (Strings.isNotEmpty(stringAttribute(def,"exit")) && !(parent instanceof SequenceElementType)) {
            log.warn('[' + getLanguageDialect().getID() + "] Invalid element attribute 'exit'. (id=" + this.id + "). Attribute is only allowed for direct child of sequence element");
        }
        loadDefinition(def);
    }

    Set<BranchCheck> parseBranchChecks(String definitions) {
        Set<BranchCheck> branches = null;
        if (definitions != null) {
            branches = new HashSet<>();
            StringTokenizer tokenizer = new StringTokenizer(definitions, " ");
            while (tokenizer.hasMoreTokens()) {
                String branchDef = tokenizer.nextToken().trim();
                branches.add(new BranchCheck(branchDef));
            }
        }
        return branches;
    }

    public boolean isWrappingBegin(LeafElementType elementType) {
        return wrapping != null && wrapping.getBeginElementType() == elementType;
    }

    @Override
    public boolean isWrappingBegin(TokenType tokenType) {
        return wrapping != null && wrapping.getBeginElementType().getTokenType() == tokenType;
    }

    public boolean isWrappingEnd(LeafElementType elementType) {
        return wrapping != null && wrapping.getEndElementType() == elementType;
    }

    @Override
    public boolean isWrappingEnd(TokenType tokenType) {
        return wrapping != null && wrapping.getEndElementType().getTokenType() == tokenType;
    }

    protected abstract ElementTypeLookupCache<?> createLookupCache();

    @NotNull
    protected abstract ElementTypeParser createParser();

    @Override
    public void setDefaultFormatting(FormattingDefinition defaultFormatting) {
        formatting = FormattingDefinitionFactory.mergeDefinitions(formatting, defaultFormatting);
    }

    protected void loadDefinition(Element def) throws ElementTypeDefinitionException {
        String attributesString = stringAttribute(def, "attributes");
        if (Strings.isNotEmptyOrSpaces(attributesString)) {
            attributes =  new ElementTypeAttributeHolder(attributesString);
        }

        String objectTypeName = stringAttribute(def, "virtual-object");
        if (objectTypeName != null) {
            virtualObjectType = ElementTypeBundle.resolveObjectType(objectTypeName);
        }
        formatting = FormattingDefinitionFactory.loadDefinition(def);
        if (is(ElementTypeAttribute.STATEMENT)) {
            setDefaultFormatting(STATEMENT_FORMATTING);
        }

        String iconKey = stringAttribute(def, "icon");
        if (iconKey != null)  icon = Icons.getIcon(iconKey);

        String branchDef = stringAttribute(def, "branch");
        if (branchDef != null) {
            branch = new Branch(branchDef);
        }

        loadWrappingAttributes(def);
    }

    private void loadWrappingAttributes(Element def) {
        String optionalWrapping = stringAttribute(def, "optional-wrapping");
        TokenElementType beginTokenElement = null;
        TokenElementType endTokenElement = null;
        if (Strings.isEmpty(optionalWrapping)) {
            String beginTokenId = stringAttribute(def, "wrapping-begin-token");
            String endTokenId = stringAttribute(def, "wrapping-end-token");

            if (Strings.isNotEmpty(beginTokenId) && Strings.isNotEmpty(endTokenId)) {
                beginTokenElement = new TokenElementType(bundle, this, beginTokenId, id);
                endTokenElement = new TokenElementType(bundle, this, endTokenId, id);
            }
        } else {
            TokenPairTemplate template = TokenPairTemplate.valueOf(optionalWrapping);
            String beginTokenId = template.getBeginToken();
            String endTokenId = template.getEndToken();
            beginTokenElement = new TokenElementType(bundle, this, beginTokenId, id);
            endTokenElement = new TokenElementType(bundle, this, endTokenId, id);

            if (template.isBlock()) {
                beginTokenElement.setDefaultFormatting(FormattingDefinition.LINE_BREAK_AFTER);
                endTokenElement.setDefaultFormatting(FormattingDefinition.LINE_BREAK_BEFORE);
                setDefaultFormatting(FormattingDefinition.LINE_BREAK_BEFORE);
            }
        }

        if (beginTokenElement != null) {
            wrapping = new WrappingDefinition(beginTokenElement, endTokenElement);
        }

        scopeDemarcation = is(ElementTypeAttribute.SCOPE_DEMARCATION) || is(ElementTypeAttribute.STATEMENT);
        scopeIsolation = is(ElementTypeAttribute.SCOPE_ISOLATION);
    }

    @Override
    public boolean is(ElementTypeAttribute attribute) {
        return attributes != null && attributes.is(attribute);
    }

    @Override
    public boolean set(ElementTypeAttribute attribute, boolean value) {
        throw new AbstractMethodError("Operation not allowed");
    }

    @Override
    @NotNull
    public DBLanguage getLanguage() {
        return getLanguageDialect().getBaseLanguage();
    }

    @Override
    public DBLanguageDialect getLanguageDialect() {
        return (DBLanguageDialect) super.getLanguage();
    }

    @Override
    public ElementTypeBundle getElementBundle() {
        return bundle;
    }

    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public int getIndexInParent(LanguageNode node) {
        LanguageNode parentNode = node.getParent();
        if (parentNode != null && parentNode.getElement() instanceof SequenceElementType) {
            SequenceElementType sequenceElementType = (SequenceElementType) parentNode.getElement();
            return sequenceElementType.indexOf(this);
        }
        return 0;
    }

    /*********************************************************
     *                  Virtual Object                       *
     *********************************************************/
    @Override
    public boolean isVirtualObject() {
        return virtualObjectType != null;
    }

    @Override
    public DBObjectType getVirtualObjectType() {
        return virtualObjectType;
    }

    protected boolean getBooleanAttribute(Element element, String attributeName) {
        String attributeValue = stringAttribute(element, attributeName);
        if (Strings.isNotEmpty(attributeValue)) {
            if (Objects.equals(attributeValue, "true")) return true;
            if (Objects.equals(attributeValue, "false")) return false;
            log.warn('[' + getLanguageDialect().getID() + "] Invalid element boolean attribute '" + attributeName + "' (id=" + this.id + "). Expected 'true' or 'false'");
        }
        return false;
    }

    @Override
    public TokenType getTokenType() {
        return null;
    }

    public void collectLeafElements(Set<LeafElementType> bucket) {
        if (wrapping != null) {
            bucket.add(wrapping.getBeginElementType());
            bucket.add(wrapping.getEndElementType());
        }
    }
}
