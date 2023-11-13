package com.dbn.language.common.element;

import com.dbn.language.common.DBLanguage;
import com.dbn.language.common.element.cache.ElementTypeLookupCache;
import com.dbn.language.common.element.impl.LeafElementType;
import com.dbn.language.common.element.impl.WrappingDefinition;
import com.dbn.language.common.element.parser.ElementTypeParser;
import com.dbn.language.common.element.path.LanguageNode;
import com.dbn.code.common.style.formatting.FormattingDefinition;
import com.dbn.common.property.PropertyHolder;
import com.dbn.language.common.DBLanguageDialect;
import com.dbn.language.common.TokenType;
import com.dbn.language.common.element.parser.Branch;
import com.dbn.language.common.element.util.ElementTypeAttribute;
import com.dbn.object.type.DBObjectType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Set;

public interface ElementType extends PropertyHolder<ElementTypeAttribute>{

    @NotNull
    String getId();

    @NotNull
    String getName();

    @NotNull
    default String getDebugName() {
        return getName();
    }

    String getDescription();

    Icon getIcon();

    ElementType getParent();

    DBLanguage getLanguage();

    DBLanguageDialect getLanguageDialect();

    ElementTypeLookupCache getLookupCache();

    ElementTypeParser getParser();

    boolean isLeaf();

    boolean isVirtualObject();

    DBObjectType getVirtualObjectType();

    PsiElement createPsiElement(ASTNode astNode);

    ElementTypeBundle getElementBundle();

    FormattingDefinition getFormatting();

    void setDefaultFormatting(FormattingDefinition defaults);

    WrappingDefinition getWrapping();

    boolean isWrappingBegin(LeafElementType elementType);

    boolean isWrappingBegin(TokenType tokenType);

    boolean isWrappingEnd(LeafElementType elementType);

    boolean isWrappingEnd(TokenType tokenType);

    int getIndexInParent(LanguageNode node);

    @Nullable
    Branch getBranch();

    boolean isScopeDemarcation();

    boolean isScopeIsolation();

    TokenType getTokenType();

    default void collectLeafElements(Set<LeafElementType> leafElementTypes) {};
}
