package com.dbn.code.psql.color;

import com.dbn.code.common.color.DBLColorSettingsPage;
import com.dbn.common.icon.Icons;
import com.dbn.language.psql.PSQLLanguage;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PSQLColorSettingsPage extends DBLColorSettingsPage {
    public PSQLColorSettingsPage() {
        attributeDescriptors.add(new AttributesDescriptor("Line comment", PSQLTextAttributesKeys.LINE_COMMENT));
        attributeDescriptors.add(new AttributesDescriptor("Block comment", PSQLTextAttributesKeys.BLOCK_COMMENT));
        attributeDescriptors.add(new AttributesDescriptor("String literal", PSQLTextAttributesKeys.STRING));
        attributeDescriptors.add(new AttributesDescriptor("Numeric literal", PSQLTextAttributesKeys.NUMBER));
        attributeDescriptors.add(new AttributesDescriptor("Alias", PSQLTextAttributesKeys.ALIAS));
        attributeDescriptors.add(new AttributesDescriptor("Identifier", PSQLTextAttributesKeys.IDENTIFIER));
        attributeDescriptors.add(new AttributesDescriptor("Quoted identifier", PSQLTextAttributesKeys.QUOTED_IDENTIFIER));
        attributeDescriptors.add(new AttributesDescriptor("Keyword", PSQLTextAttributesKeys.KEYWORD));
        attributeDescriptors.add(new AttributesDescriptor("Function", PSQLTextAttributesKeys.FUNCTION));
        attributeDescriptors.add(new AttributesDescriptor("DataType", PSQLTextAttributesKeys.DATA_TYPE));
        attributeDescriptors.add(new AttributesDescriptor("Parenthesis", PSQLTextAttributesKeys.PARENTHESIS));
        attributeDescriptors.add(new AttributesDescriptor("Exception", PSQLTextAttributesKeys.EXCEPTION));
        attributeDescriptors.add(new AttributesDescriptor("Bracket", PSQLTextAttributesKeys.BRACKET));
        attributeDescriptors.add(new AttributesDescriptor("Operator", PSQLTextAttributesKeys.OPERATOR));
    }

    @Override
    @NotNull
    public String getDisplayName() {
        return "PL/SQL (DBN)";
    }
    @Override
    @Nullable
    public Icon getIcon() {
        return Icons.FILE_PLSQL;
    }

    @Override
    @NotNull
    public SyntaxHighlighter getHighlighter() {
        return PSQLLanguage.INSTANCE.getMainLanguageDialect().getSyntaxHighlighter();
    }

    @Override
    public String getDemoTextFileName() {
        return "plsql_demo_text.txt";  
    }
}