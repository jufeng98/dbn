package com.dci.intellij.dbn.data.grid.color;

import com.dci.intellij.dbn.common.icon.Icons;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.PlainSyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataGridColorSettingsPage implements ColorSettingsPage {
    protected final List<AttributesDescriptor> attributeDescriptors = new ArrayList<>();
    protected final List<ColorDescriptor> colorDescriptors = new ArrayList<>();

    public DataGridColorSettingsPage() {
        attributeDescriptors.add(new AttributesDescriptor("Plain Data", DataGridTextAttributesKeys.PLAIN_DATA));
        attributeDescriptors.add(new AttributesDescriptor("Audit Data", DataGridTextAttributesKeys.AUDIT_DATA));
        attributeDescriptors.add(new AttributesDescriptor("Modified Data", DataGridTextAttributesKeys.MODIFIED_DATA));
        attributeDescriptors.add(new AttributesDescriptor("Deleted Data", DataGridTextAttributesKeys.DELETED_DATA));
        attributeDescriptors.add(new AttributesDescriptor("Error Data", DataGridTextAttributesKeys.ERROR_DATA));
        attributeDescriptors.add(new AttributesDescriptor("Readonly Data", DataGridTextAttributesKeys.READONLY_DATA));
        attributeDescriptors.add(new AttributesDescriptor("Loading Data", DataGridTextAttributesKeys.LOADING_DATA));
        attributeDescriptors.add(new AttributesDescriptor("Primary Key", DataGridTextAttributesKeys.PRIMARY_KEY));
        attributeDescriptors.add(new AttributesDescriptor("Foreign Key", DataGridTextAttributesKeys.FOREIGN_KEY));
        attributeDescriptors.add(new AttributesDescriptor("Selection", DataGridTextAttributesKeys.SELECTION));
        attributeDescriptors.add(new AttributesDescriptor("Caret Row", DataGridTextAttributesKeys.CARET_ROW));
    }

    @Override
    public Icon getIcon() {
        return Icons.DBO_TABLE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new PlainSyntaxHighlighter();
    }

    @Override
    @NonNls
    @NotNull
    public final String getDemoText() {
        return " ";
    }

    @Override
    @NotNull
    public AttributesDescriptor[] getAttributeDescriptors() {
        return attributeDescriptors.toArray(new AttributesDescriptor[0]);
    }

    @Override
    @NotNull
    public ColorDescriptor[] getColorDescriptors() {
        return colorDescriptors.toArray(new ColorDescriptor[0]);
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Data Grid (DBN)";
    }

    @Override
    @Nullable
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }
}
