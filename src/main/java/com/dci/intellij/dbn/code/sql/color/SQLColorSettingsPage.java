package com.dci.intellij.dbn.code.sql.color;

import com.dci.intellij.dbn.code.common.color.DBLColorSettingsPage;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.language.sql.SQLLanguage;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SQLColorSettingsPage extends DBLColorSettingsPage {

    public SQLColorSettingsPage() {
        attributeDescriptors.add(new AttributesDescriptor("Line Comment", SQLTextAttributesKeys.LINE_COMMENT));
        attributeDescriptors.add(new AttributesDescriptor("Block Comment", SQLTextAttributesKeys.BLOCK_COMMENT));
        attributeDescriptors.add(new AttributesDescriptor("String", SQLTextAttributesKeys.STRING));
        attributeDescriptors.add(new AttributesDescriptor("Number", SQLTextAttributesKeys.NUMBER));
        attributeDescriptors.add(new AttributesDescriptor("Alias", SQLTextAttributesKeys.ALIAS));
        attributeDescriptors.add(new AttributesDescriptor("Identifier", SQLTextAttributesKeys.IDENTIFIER));
        attributeDescriptors.add(new AttributesDescriptor("Quoted Identifier", SQLTextAttributesKeys.QUOTED_IDENTIFIER));
        attributeDescriptors.add(new AttributesDescriptor("Keyword", SQLTextAttributesKeys.KEYWORD));
        attributeDescriptors.add(new AttributesDescriptor("Function", SQLTextAttributesKeys.FUNCTION));
        attributeDescriptors.add(new AttributesDescriptor("Parameter", SQLTextAttributesKeys.PARAMETER));
        attributeDescriptors.add(new AttributesDescriptor("DataType", SQLTextAttributesKeys.DATA_TYPE));
        attributeDescriptors.add(new AttributesDescriptor("Parenthesis", SQLTextAttributesKeys.PARENTHESIS));
        attributeDescriptors.add(new AttributesDescriptor("Bracket", SQLTextAttributesKeys.BRACKET));
        attributeDescriptors.add(new AttributesDescriptor("Operator", SQLTextAttributesKeys.OPERATOR));
        attributeDescriptors.add(new AttributesDescriptor("Execution Variable", SQLTextAttributesKeys.VARIABLE));
        attributeDescriptors.add(new AttributesDescriptor("Procedural Block", SQLTextAttributesKeys.CHAMELEON));
    }


    @Override
    @NotNull
    public String getDisplayName() {
        return "SQL (DBN)";
    }

    @Override
    @Nullable
    public Icon getIcon() {
        return Icons.FILE_SQL;
    }

    @Override
    @NotNull
    public SyntaxHighlighter getHighlighter() {
        return SQLLanguage.INSTANCE.getMainLanguageDialect().getSyntaxHighlighter();
    }

    @Override
    public String getDemoTextFileName() {
        return "sql_demo_text.txt";
    }

}
