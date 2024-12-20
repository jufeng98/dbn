package com.dbn.mybatis.custom;


import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.project.Project;
import com.intellij.ui.HorizontalScrollBarEditorCustomization;
import com.intellij.ui.LanguageTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JavaEditorTextField extends LanguageTextField {

    public JavaEditorTextField(@Nullable Project project, String javaCode) {
        super(JavaLanguage.INSTANCE, project, javaCode, new MyDocumentCreator(), false);
    }

    @Override
    protected @NotNull EditorEx createEditor() {
        EditorEx editor = super.createEditor();

        editor.setHorizontalScrollbarVisible(true);
        editor.setVerticalScrollbarVisible(true);
        HorizontalScrollBarEditorCustomization.ENABLED.customize(editor);

        EditorSettings settings = editor.getSettings();
        settings.setLineNumbersShown(true);
        settings.setFoldingOutlineShown(true);
        settings.setAllowSingleLogicalLineFolding(true);
        settings.setRightMarginShown(true);
        return editor;
    }

}